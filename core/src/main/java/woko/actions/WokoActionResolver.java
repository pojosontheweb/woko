package woko.actions;

import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.controller.DefaultViewActionBean;
import net.sourceforge.stripes.controller.NameBasedActionResolver;
import net.sourceforge.stripes.exception.StripesServletException;
import net.sourceforge.stripes.util.ReflectUtil;
import woko.Woko;
import woko.facets.ResolutionFacet;
import woko.persistence.ObjectStore;
import woko.util.WLogger;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Collection;

public class WokoActionResolver extends NameBasedActionResolver {

  private static final WLogger logger = WLogger.getLogger(WokoActionResolver.class);

  private Woko woko = null;

  @Override
  public void init(Configuration configuration) throws Exception {
    super.init(configuration);
  }

  @Override
  protected ActionBean handleActionBeanNotFound(ActionBeanContext context, String urlBinding) {
    WokoActionBeanContext abc = (WokoActionBeanContext)context;
    if (woko==null) {
      woko = abc.getWoko();
    }
    // grab the appropriate facet from context
    HttpServletRequest req = context.getRequest();
    // TODO request split and get facet params (clean URLs) ?
    String className = req.getParameter("className");
    String facetName = req.getParameter("facetName");
    String key = req.getParameter("key");
    if (facetName==null) {
      // try clean URL mode
      String servletPath = req.getServletPath();
      if (servletPath.startsWith("/")) {
        servletPath = servletPath.substring(1, servletPath.length());
      }
      String[] path = servletPath.split("/");
      if (path.length>0) {
        facetName = path[0];
        if (path.length>1 && className==null) {
          className = path[1];
          if (path.length>2 && key==null) {
            key = path[2];
          }
        }
      }
    }

    if (facetName==null) {
      logger.warn("Facet not found, name not specified");
      super.handleActionBeanNotFound(context, urlBinding);
    }

    logger.debug("Loading object for className=" + className + " and key=" + key);
    ObjectStore objectStore = woko.getObjectStore();
    Object object = objectStore.load(className,key);
    logger.debug("Loaded " + object + " (className=" + className + ", key=" + key + ")");
    Class targetObjectClass;
    if (object!=null) {
      targetObjectClass = object.getClass();
    } else {
      targetObjectClass = objectStore.getMappedClass(className);
    }
    Object f = woko.getFacet(facetName, req, object, targetObjectClass);
    if (f==null) {
      String username = woko.getUsername(req);
      logger.warn("Unable to find facet for name=" + facetName + ", targetObject=" + object + ", targetObjectClass=" + targetObjectClass + " and key=" + key +
        ", username=" + username);
      return super.handleActionBeanNotFound(context,urlBinding);
    }
    if (!(f instanceof ActionBean)) {
      throw new IllegalStateException("Facet " + f + " of class " + f.getClass() + " does not implement ActionBean.");
    } else {
      ActionBean facet = (ActionBean)f;
      facet.setContext(abc);
      String path = "/" + facetName;
      if (targetObjectClass!=null) {
        path += "/" + objectStore.getClassMapping(targetObjectClass);
        if (key!=null) {
          path += "/" + key;
        }
      }

      req.setAttribute(RESOLVED_ACTION, path);
      logger.debug("Returning faceted action bean " + facet);
      return facet;
    }
  }

  @Override
  public Method getDefaultHandler(Class<? extends ActionBean> beanClass) throws StripesServletException {
    if (ResolutionFacet.class.isAssignableFrom(beanClass)) {
      // find method
      Collection<Method> methods = ReflectUtil.getMethods(beanClass);
      for (Method m : methods) {
        if (m.getName().equals("getResolution")) {
          return m;
        }
      }
    }
    return super.getDefaultHandler(beanClass);
  }

  @Override
  public String getUrlBinding(Class<? extends ActionBean> clazz) {
    if (ResolutionFacet.class.isAssignableFrom(clazz)) {
      FacetDescriptor[] allDescriptors = woko.getJFacets().getFacetRepository().getFacetDescriptorManager().getDescriptors();
      for (FacetDescriptor fd : allDescriptors) {
        if (fd.getFacetClass().equals(clazz)) {
          return fd.getName();
        }
      }
    }
    return super.getUrlBinding(clazz);
  }
}

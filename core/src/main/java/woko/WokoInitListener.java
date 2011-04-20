package woko;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import woko.persistence.ObjectStore;
import woko.users.RemoteUserStrategy;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;
import woko.util.WLogger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class WokoInitListener implements ServletContextListener {

  public static final String CTX_PARAM_FACET_PACKAGES = "Woko.Facet.Packages";

  private static final WLogger logger = WLogger.getLogger(WokoInitListener.class);

  private ServletContext servletContext;

  public ServletContext getServletContext() {
    return servletContext;
  }

  public void contextInitialized(ServletContextEvent e) {
    servletContext = e.getServletContext();
    servletContext.setAttribute(Woko.CTX_KEY, createWoko());
  }

  public void contextDestroyed(ServletContextEvent e) {
    Woko woko = Woko.getWoko(e.getServletContext());
    if (woko!=null) {
      woko.close();
    }
  }

  protected Woko createWoko() {
    return new Woko(
        createObjectStore(),
        createUserManager(),
        createFallbackRoles(),
        createFacetDescriptorManager(),
        createUsernameResolutionStrategy());
  }

  protected List<String> createFallbackRoles() {
    return Arrays.asList(Woko.ROLE_GUEST);
  }

  protected IFacetDescriptorManager createFacetDescriptorManager() {
    List<String> pkgs = new ArrayList<String>(Woko.DEFAULT_FACET_PACKAGES);
    List<String> packagesNames = getPackageNamesFromConfig(CTX_PARAM_FACET_PACKAGES, false);
    if (packagesNames!=null && packagesNames.size()>0) {
      pkgs.addAll(packagesNames);
    }
    return Woko.createFacetDescriptorManager(pkgs);
  }

  protected abstract ObjectStore createObjectStore();

  protected abstract UserManager createUserManager();

  protected UsernameResolutionStrategy createUsernameResolutionStrategy() {
    return new RemoteUserStrategy();
  }

  protected List<String> getPackageNamesFromConfig(String paramName) {
    return getPackageNamesFromConfig(paramName, true);
  }

  protected List<String> getPackageNamesFromConfig(String paramName, boolean throwIfNotFound) {
    String pkgNamesStr = getServletContext().getInitParameter(paramName);
    if (pkgNamesStr==null || pkgNamesStr.equals("")) {
      if (throwIfNotFound) {
        String msg = "No package names specified. You have to set the context init-param " +
            paramName + " in web.xml to the list of packages you want to be scanned for pesistent classes.";
        logger.error(msg);
        throw new IllegalStateException(msg);
      } else {
        return Collections.emptyList();
      }
    }
    String[] pkgNamesArr = pkgNamesStr.
            replace('\n', ',').
            replace(' ', ',').
            split(",");
    List<String> pkgNames = new ArrayList<String>();
    for (String s : pkgNamesArr) {
      if (s!=null && !s.equals("")) {
        pkgNames.add(s);
      }
    }
    return pkgNames;
  }

}

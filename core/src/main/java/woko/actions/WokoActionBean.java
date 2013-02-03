/*
 * Copyright 2001-2012 Remi Vankeisbelck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package woko.actions;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import woko.Woko;
import woko.facets.FacetNotFoundException;
import woko.facets.ResolutionFacet;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;
import woko.util.WLogger;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * WokoActionBean is the heart of Woko : it handles incoming requests by :
 * <el>
 *     <li>Loading the target object using <code>className</code> and <code>key</code> request parameters</li>
 *     <li>Retrieving the {@link ResolutionFacet} for request parameter <code>facetName</code> and loaded target object</li>
 *     <li>Perform dynamic validation on the facet and target object</li>
 *     <li>Invoke the facet's event handler</li>
 * </el>
 *
 * This action is bound to <code>/{facetName}/{className}/{key}</code>, which provides a consistent URL scheme. DMF should
 * be used in order to make it 0-config.
 *
 * <h2>Target Object loading (before binding/validation)</h2>
 *
 * Grab <code>className</code> and <code>key</code> request parameters and try to load
 * the target object using configured {@link ObjectStore}.
 * If <code>key</code> is not supplied, then look for <code>createTransient</code> request param and
 * create new instance of the specified <code>className</code> using no-args constructor.
 * If <code>createTransient</code> is not specified, then don't create or load the target object at all.
 *
 * <h2>Resolution Facet loading (before binding/validation)</h2>
 *
 * Using <code>facetName</code> request parameter, try to load a {@link ResolutionFacet} for loaded target
 * object (or target object class, in case no target object was loaded).
 *
 * If the resolution facet is found, then invoke its event handler (using request param names, "a la Stripes").
 *
 * If no resolution facet is found, then throw a {@link FacetNotFoundException}.
 */
@UrlBinding("/{facetName}/{className}/{key}")
public class WokoActionBean<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseActionBean<OsType,UmType,UnsType,FdmType> {

    private static final WLogger logger = WLogger.getLogger(WokoActionBean.class);

    public static final String CREATE_TRANSIENT_REQ_PARAM = "createTransient";

    private String className;
    private String key;
    @Validate(required = true)
    private String facetName;

    @ValidateNestedProperties({})
    private Object object;
    @ValidateNestedProperties({})
    private ResolutionFacet facet;

    private Method eventHandlerMethod = null;

    /**
     * Return the target object, loaded before binding and validation.
     * @return the target object if any (null otherwise)
     */
    public Object getObject() {
        return object;
    }

    /**
     * Return the resolution facet, loaded before binding and validation.
     * @return the resolution facet
     */
    public ResolutionFacet getFacet() {
        return facet;
    }

    /**
     * Return the <code>className</code> used to load the target object (bound request param)
     * @return the target object's <code>className</code>
     */
    public String getClassName() {
        return className;
    }

    /**
     * Set the <code>className</code> used to load the target object (bound request param)
     * @param className the target object's <code>className</code>
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Return the <code>key</code> (identifier) used to load the target object (bound request param)
     * @return the target object's <code>key</code>
     */
    public String getKey() {
        return key;
    }

    /**
     * Set the <code>key</code> (identifier) used to load the target object (bound request param)
     * @param key the target object's <code>key</code>
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Return the <code>facetName</code> used to load the resolution facet (bound request param)
     * @return the <code>facetName</code>
     */
    public String getFacetName() {
        return facetName;
    }

    /**
     * Set the <code>facetName</code> used to load the resolution facet (bound request param)
     * @param facetName the <code>facetName</code>
     */
    public void setFacetName(String facetName) {
        this.facetName = facetName;
    }

    /**
     * Interceptor method that loads the target object and facet.
     */
    @Before(stages = {LifecycleStage.BindingAndValidation})
    public void loadObjectAndFacet() {
        HttpServletRequest req = getContext().getRequest();
        className = req.getParameter("className");
        facetName = req.getParameter("facetName");
        if (facetName == null) {
            throw new RuntimeException("facetName parameter not found in request");
        }
        key = req.getParameter("key");
        logger.debug("Loading object for className=" + className + " and key=" + key);
        Woko<OsType,UmType,UnsType,FdmType> woko = getContext().getWoko();
        OsType objectStore = woko.getObjectStore();
        if (className!=null) {
            if (key!=null) {
                object = objectStore.load(className, key);
                logger.debug("Loaded " + object + " (className=" + className + ", key=" + key + ")");
            } else {
                String createTransientStr = req.getParameter(CREATE_TRANSIENT_REQ_PARAM);
                boolean createTransient = createTransientStr!=null && createTransientStr.equals("true");
                if (createTransient) {
                    Class<?> mappedClass = objectStore.getMappedClass(className);
                    try {
                        object = mappedClass.newInstance();
                        logger.debug("Created transient " + object + ", no key provided (className=" + className + ")");
                    } catch (Exception e) {
                        logger.error("Unable to create instance of " + mappedClass + " using no-args constructor.", e);
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        Class targetObjectClass;
        if (object != null) {
            targetObjectClass = object.getClass();
        } else if (className!=null) {
            targetObjectClass = objectStore.getMappedClass(className);
        } else {
            targetObjectClass = Object.class;
        }
        Object f = woko.getFacet(facetName, req, object, targetObjectClass);
        if (f == null) {
            String username = woko.getUsername(req);
            throw new FacetNotFoundException(facetName, className, key, username);
        }
        if (!(f instanceof ResolutionFacet)) {
            throw new IllegalStateException("Facet " + f + " of class " + f.getClass() + " does not implement ResolutionFacet.");
        }
        facet = (ResolutionFacet) f;
        logger.debug("Resolution facet " + facet + " loaded");
    }

    /**
     * Resolve the <code>ResolutionFacet</code>'s handler method and invoke it.
     *
     * @return the <code>Resolution</code> returned by the <code>ResolutionFacet</code>'s event handler
     */
    @DefaultHandler
    public Resolution execute() {
        Method handler = getEventHandlerMethod();
        try {
            logger.debug("Executing handler method : " + facet.toString() + "." + handler.getName());            
            Object[] params;
            Class<?>[] paramTypes = handler.getParameterTypes();
            if (paramTypes.length==1) {
                params = new Object[] { getContext() };
            } else {
                params = new Object[0];
            }
            Resolution result = (Resolution)handler.invoke(facet, params);
            if (result==null) {
                String msg = "Execution of facet " + facet + " returned null (using handler '" + handler.getName() + "')";
                logger.error(msg);
                throw new IllegalStateException(msg);
            }
            return result;
        } catch (Exception e) {
            String msg = "Invocation of handler method " + facet.getClass().getName() +
                    "." + handler.getName() + " threw Exception";
            logger.error(msg, e);
            if (e instanceof InvocationTargetException) {
                Throwable target = ((InvocationTargetException)e).getTargetException();
                if (target instanceof RuntimeException) {
                    throw (RuntimeException)target;
                } else {
                    throw new RuntimeException(e);
                }
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Resolve the even handler method on the <code>ResolutionFacet</code> using request parameter names.
     *
     * @return the event handler method
     */
    public Method getEventHandlerMethod() {
        if (eventHandlerMethod==null) {

            @SuppressWarnings("unchecked")
            Set<String> requestParamNames =
                    new HashSet<String>(getContext().getRequest().getParameterMap().keySet());

            // find the method handler in the facet
            List<Method> matchingMethods = new ArrayList<Method>();
            for (Method m : facet.getClass().getMethods()) {
                if (Modifier.isPublic(m.getModifiers()) && Resolution.class.isAssignableFrom(m.getReturnType())) {
                    Class<?>[] paramTypes = m.getParameterTypes();
                    if (paramTypes.length==0 || paramTypes.length==1 && ActionBeanContext.class.isAssignableFrom(paramTypes[0])) {
                        // method signature is ok, check if we have a request parameter with that name !
                        if (requestParamNames.contains(m.getName())) {
                            matchingMethods.add(m);
                        }
                    }
                }
            }

            int nbMatchingMethods = matchingMethods.size();
            if (nbMatchingMethods>1) {
                // check that we have only 1 handler matching
                StringBuilder msg = new StringBuilder();
                msg.append("More than 1 handler method found in ResolutionFacet : ")
                        .append(facet.getClass().getName())
                        .append(" : \n");
                for (Method m : matchingMethods) {
                    msg.append("  * ").append(m.getName()).append("\n");
                }
                throw new IllegalStateException(msg.toString());
            } else if (nbMatchingMethods==0) {
                // default to interface method
                try {
                    eventHandlerMethod = facet.getClass().getMethod("getResolution", ActionBeanContext.class);
                } catch (NoSuchMethodException e) {
                    // should never happen unless we refactor getResolution()...
                    throw new RuntimeException(e);
                }

            } else {
                // 1 handler matched, just return this one
                eventHandlerMethod = matchingMethods.get(0);
            }
        }
        return eventHandlerMethod;
    }
}

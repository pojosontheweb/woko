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

    public Object getObject() {
        return object;
    }

    public ResolutionFacet getFacet() {
        return facet;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFacetName() {
        return facetName;
    }

    public void setFacetName(String facetName) {
        this.facetName = facetName;
    }

    // cache for handlerMethod (don't lookup for each call)
    private Method handlerMethod = null;


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

    @DefaultHandler
    public Resolution execute() {
        Method handler = getEventHandlerMethod();
        try {
            Resolution result = (Resolution)handler.invoke(facet, getContext());
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

    public Method getEventHandlerMethod() {
        if (handlerMethod==null) {
            // cache request param names
            @SuppressWarnings("unchecked")
            Set<String> requestParamNames =
                    new HashSet<String>(getContext().getRequest().getParameterMap().keySet());

            // find the method handler in the facet
            List<Method> matchingMethods = new ArrayList<Method>();
            for (Method m : facet.getClass().getMethods()) {
                if (Modifier.isPublic(m.getModifiers()) && Resolution.class.isAssignableFrom(m.getReturnType())) {
                    Class<?>[] paramTypes = m.getParameterTypes();
                    if (paramTypes.length==1 && ActionBeanContext.class.isAssignableFrom(paramTypes[0])) {
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
                    handlerMethod = facet.getClass().getMethod("getResolution", ActionBeanContext.class);
                } catch (NoSuchMethodException e) {
                    // should never happen unless we refactor getResolution()...
                    throw new RuntimeException(e);
                }

            } else {
                // 1 handler matched, just return this one
                handlerMethod = matchingMethods.get(0);
            }
        }
        return handlerMethod;
    }
}

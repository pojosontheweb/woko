/*
 * Copyright 2001-2010 Remi Vankeisbelck
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

import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import woko.Woko;
import woko.facets.FacetNotFoundException;
import woko.facets.ResolutionFacet;
import woko.persistence.ObjectStore;
import woko.util.WLogger;

import javax.servlet.http.HttpServletRequest;

@UrlBinding("/{facetName}/{className}/{key}")
public class WokoActionBean extends BaseActionBean {

    private static final WLogger logger = WLogger.getLogger(WokoActionBean.class);

    private String className;
    private String key;
    @Validate(required = true)
    private String facetName;
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
        Woko woko = getContext().getWoko();
        ObjectStore objectStore = woko.getObjectStore();
        object = objectStore.load(className, key);
        logger.debug("Loaded " + object + " (className=" + className + ", key=" + key + ")");
        Class targetObjectClass;
        if (object != null) {
            targetObjectClass = object.getClass();
        } else {
            targetObjectClass = objectStore.getMappedClass(className);
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
        Resolution result = facet.getResolution(getContext());
        if (result == null) {
            throw new IllegalStateException("Execution of facet " + facet + " returned null !");
        }
        return result;
    }

}

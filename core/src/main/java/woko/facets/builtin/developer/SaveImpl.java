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

package woko.facets.builtin.developer;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.rpc.RpcResolutionWrapper;
import woko.Woko;
import woko.facets.BaseResolutionFacet;
import woko.facets.WokoFacetContext;
import woko.facets.builtin.*;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;
import woko.util.WLogger;

/**
 * Generic <code>save</code> facet : saves or updates any Woko-managed POJO to the Store.
 *
 * Available only to <code>developer</code> users by default. Override for your role(s) in
 * order to make this available for your users.
 */
@FacetKey(name = WokoFacets.save, profileId = "developer")
public class SaveImpl<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseResolutionFacet<OsType,UmType,UnsType,FdmType> implements Save {

    private final static WLogger logger = WLogger.getLogger(SaveImpl.class);

    public static final String TARGET_FACET_AFTER_SAVE = WokoFacets.edit;

    /**
     * Perform validation (using <code>validate</code> facet) and save the target object.
     * @param abc the action bean context
     * @return a Resolution that redirects to the post-save page, or returns the updated object as JSON (RPC resolution)
     */
    public Resolution getResolution(final ActionBeanContext abc) {
        // try to find a validation facet for the object
        final WokoFacetContext<OsType,UmType,UnsType,FdmType> facetContext = getFacetContext();
        final Woko<OsType,UmType,UnsType,FdmType> woko = facetContext.getWoko();
        final Object targetObject = facetContext.getTargetObject();
        Class<?> clazz = targetObject.getClass();
        Validate validateFacet = woko.getFacet(WokoFacets.validate, abc.getRequest(), targetObject, clazz);
        if (validateFacet != null) {
            logger.debug("Validation facet found, validating before saving...");
            if (validateFacet.validate(abc)) {
                // validation OK, store nb errors before call to doSave()
                int nbErrs = abc.getValidationErrors().size();
                doSave(abc);
                if (abc.getValidationErrors().size()!=nbErrs) {
                    logger.debug("doSave raised validation errors, not saving");
                    // forward to the edit fragment
                    Edit editFacet = woko.getFacet(WokoFacets.edit, abc.getRequest(), targetObject, clazz, true);
                    return new ForwardResolution(editFacet.getFragmentPath());
                }
            } else {
                logger.debug("Validate facet raised validation errors, not saving");
                // forward to the edit fragment
                Edit editFacet = woko.getFacet(WokoFacets.edit, abc.getRequest(), targetObject, clazz, true);
                return new ForwardResolution(editFacet.getFragmentPath());
            }
        } else {
            logger.debug("No validation facet found, saving...");
            doSave(abc);
        }

        Resolution resolution = getNonRpcResolution(abc);

        return new RpcResolutionWrapper(resolution) {
            @Override
            public Resolution getRpcResolution() {
                Json json = woko.getFacet(WokoFacets.json, facetContext.getRequest(), targetObject);
                return json==null ? null : json.getResolution(abc);
            }
        };

    }

    /**
     * Create and return the non-RPC resolution. Redirects to the facet returned by
     * <code>getTargetFacetAfterSave()</code>.
     * @param abc the action bean context
     * @return a RedirectResolution to the target facet after save
     */
    protected Resolution getNonRpcResolution(ActionBeanContext abc) {
        WokoFacetContext<OsType,UmType,UnsType,FdmType> fc = getFacetContext();
        return new RedirectResolution(fc.getWoko().facetUrl(getTargetFacetAfterSave(),fc.getTargetObject()));
    }

    /**
     * Return the name of the resolution facet to be used for redirect, after the target object
     * has been saved.
     * @return the name of the facet to be used for post-save redirect
     */
    protected String getTargetFacetAfterSave() {
        return TARGET_FACET_AFTER_SAVE;
    }

    /**
     * Actually save the object in the store, and toss a message to the action bean context.
     * @param abc the action bean context
     */
    protected void doSave(ActionBeanContext abc) {
        WokoFacetContext<OsType,UmType,UnsType,FdmType> facetContext = getFacetContext();
        facetContext.getWoko().getObjectStore().save(facetContext.getTargetObject());
        abc.getMessages().add(new LocalizableMessage("woko.object.saved"));
    }

}

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

@FacetKey(name = WokoFacets.save, profileId = "developer")
public class SaveImpl<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseResolutionFacet<OsType,UmType,UnsType,FdmType> implements Save {

    private final static WLogger logger = WLogger.getLogger(SaveImpl.class);

    public static final String TARGET_FACET_AFTER_SAVE = WokoFacets.edit;

    public Resolution getResolution(final ActionBeanContext abc) {
        // try to find a validation facet for the object
        final WokoFacetContext<OsType,UmType,UnsType,FdmType> facetContext = getFacetContext();
        final Woko<OsType,UmType,UnsType,FdmType> woko = facetContext.getWoko();
        final Object targetObject = facetContext.getTargetObject();
        Class<?> clazz = targetObject.getClass();
        Validate validateFacet = (Validate) woko.getFacet(WokoFacets.validate, abc.getRequest(), targetObject, clazz);
        if (validateFacet != null) {
            logger.debug("Validation facet found, validating before saving...");
            if (validateFacet.validate(abc)) {
                doSave(abc);
            } else {
                logger.debug("Validate facet raised validation errors, not saving");
                // forward to the edit fragment
                Edit editFacet = (Edit) woko.getFacet(WokoFacets.edit, abc.getRequest(), targetObject, clazz, true);
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
                Json json = (Json)woko.getFacet(WokoFacets.json, facetContext.getRequest(), targetObject);
                return json==null ? null : json.getResolution(abc);
            }
        };

    }

    protected Resolution getNonRpcResolution(ActionBeanContext abc) {
        WokoFacetContext<OsType,UmType,UnsType,FdmType> fc = getFacetContext();
        return new RedirectResolution(fc.getWoko().facetUrl(getTargetFacetAfterSave(),fc.getTargetObject()));
    }

    protected String getTargetFacetAfterSave() {
        return TARGET_FACET_AFTER_SAVE;
    }

    protected void doSave(ActionBeanContext abc) {
        WokoFacetContext<OsType,UmType,UnsType,FdmType> facetContext = getFacetContext();
        facetContext.getWoko().getObjectStore().save(facetContext.getTargetObject());
        abc.getMessages().add(new LocalizableMessage("woko.object.saved"));
    }

}

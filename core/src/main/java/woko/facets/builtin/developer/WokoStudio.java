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

import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.rpc.RpcResolutionWrapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import woko.facets.BaseForwardResolutionFacet;
import woko.facets.BaseResolutionFacet;
import woko.facets.builtin.WokoFacets;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;
import woko.util.JsonResolution;

import java.util.Arrays;
import java.util.List;

/**
 * <code>studio</code> resolution facet : provides access to the Woko Studio page.
 *
 * Available only to <code>developer</code> users by default. Override for your role(s) in
 * order to make this available for your users.
 */
@FacetKey(name = WokoFacets.studio, profileId = "developer")
public class WokoStudio<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseResolutionFacet<OsType,UmType,UnsType,FdmType> {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/developer/studio.jsp";

    @Override
    public Resolution getResolution(ActionBeanContext abc) {
        return new RpcResolutionWrapper(new ForwardResolution(FRAGMENT_PATH)) {
            @Override
            public Resolution getRpcResolution() {

                JSONObject result = new JSONObject();
                try {
                    OsType store = getWoko().getObjectStore();
                    result.put("objectStore", store.getClass().getName());
                    result.put("userManager", getWoko().getUserManager().getClass().getName());
                    result.put("fallbackRoles", new JSONArray(getWoko().getFallbackRoles()));
                    result.put("usernameResolutionStrategy", getWoko().getUsernameResolutionStrategy().getClass().getName());
                    JSONArray mappedClasses = new JSONArray();
                    for (Class<?> clazz : store.getMappedClasses()) {
                        JSONObject mappedClass = new JSONObject();
                        mappedClass.put("className", clazz.getName());
                        mappedClass.put("classMapping", store.getClassMapping(clazz));
                        mappedClasses.put(mappedClass);
                    }
                    result.put("mappedClasses", mappedClasses);
                    return new JsonResolution(result);
                } catch(JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    public Resolution descriptors() {
        JSONObject result = new JSONObject();
        JSONArray descriptors = new JSONArray();
        try {
            result.put("descriptors", descriptors);
            for (FacetDescriptor fd : getFacetDescriptors()) {
                JSONObject jsonFd = new JSONObject();
                jsonFd.put("name",fd.getName());
                jsonFd.put("profileId", fd.getProfileId());
                jsonFd.put("targetObjectType", fd.getTargetObjectType().getName());
                jsonFd.put("facetClass", fd.getFacetClass().getName());
                descriptors.put(jsonFd);
            }
            return new JsonResolution(result);
        }catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Return a list of all <code>FacetDescriptor</code>s for the app.
     * @return a list of all <code>FacetDescriptor</code>s
     */
    public List<FacetDescriptor> getFacetDescriptors() {
        FdmType fdm = getFacetContext().getWoko().getFacetDescriptorManager();
        return fdm.getDescriptors();
    }

}

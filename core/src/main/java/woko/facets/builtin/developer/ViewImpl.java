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
import net.sourceforge.jfacets.IInstanceFacet;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import woko.facets.BaseForwardResolutionFacet;
import woko.facets.BaseForwardRpcResolutionFacet;
import woko.facets.WokoFacetContext;
import woko.facets.builtin.Json;
import woko.facets.builtin.View;
import woko.facets.builtin.WokoFacets;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

/**
 * Generic <code>view</code> Resolution Facet : allows to display any Woko-managed POJO as read-only HTML.
 *
 * Available only to <code>developer</code> users by default. Override for your role(s) in
 * order to make this available for your users.
 */
@FacetKey(name = WokoFacets.view, profileId = "developer")
public class ViewImpl<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseForwardRpcResolutionFacet<OsType,UmType,UnsType,FdmType> implements View, IInstanceFacet {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/developer/view.jsp";

    public String getPath() {
        return FRAGMENT_PATH;
    }

    /**
     * Return the target object as JSON (using <code>renderObjectJson</code> if available) for RPC calls
     * @param abc the action bean context
     * @return the target object as JSON
     */
    @Override
    protected Resolution getRpcResolution(ActionBeanContext abc) {
        WokoFacetContext<OsType,UmType,UnsType,FdmType> wokoFacetContext = getFacetContext();
        Json json = wokoFacetContext.getWoko().getFacet(
                WokoFacets.json, wokoFacetContext.getRequest(), wokoFacetContext.getTargetObject());
        return json == null ? null : json.getResolution(abc);
    }

    /**
     * Don't match null target objects
     * @param targetObject the target object
     * @return <code>true</code> if <code>targetObject</code> is not null, <code>false</code> otherwise
     */
    @Override
    public boolean matchesTargetObject(Object targetObject) {
        return targetObject != null;
    }
}

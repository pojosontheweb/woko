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

package woko.facets.builtin.all;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.rpc.RpcResolutionWrapper;
import org.json.JSONObject;
import woko.facets.BaseResolutionFacet;
import woko.facets.builtin.Logout;
import woko.facets.builtin.WokoFacets;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;
import woko.util.JsonResolution;

/**
 * <code>logout</code> facet for use with built-in auth. Simply invalidates the
 * http session and redirects to <code>/home</code>
 */
@StrictBinding
@FacetKey(name= WokoFacets.logout, profileId="all")
public class LogoutImpl<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseResolutionFacet<OsType,UmType,UnsType,FdmType> implements Logout {

  public Resolution getResolution(ActionBeanContext abc) {
    abc.getRequest().getSession().invalidate();
    abc.getMessages().add(new LocalizableMessage("woko.logout.success"));
    return new RpcResolutionWrapper(new RedirectResolution("/home")) {
        @Override
        public Resolution getRpcResolution() {
            return new JsonResolution(new JSONObject());
        }
    };
  }

}

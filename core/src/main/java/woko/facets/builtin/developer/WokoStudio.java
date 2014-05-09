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
import woko.facets.BaseForwardResolutionFacet;
import woko.facets.builtin.WokoFacets;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

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
        > extends BaseForwardResolutionFacet<OsType,UmType,UnsType,FdmType> {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/developer/studio.jsp";

    public String getPath() {
        return FRAGMENT_PATH;
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

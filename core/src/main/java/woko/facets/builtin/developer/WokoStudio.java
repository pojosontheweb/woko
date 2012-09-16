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

@FacetKey(name = WokoFacets.studio, profileId = "developer")
public class WokoStudio<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseForwardResolutionFacet {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/developer/studio.jsp";

    public String getPath() {
        return FRAGMENT_PATH;
    }

    public List<FacetDescriptor> getFacetDescriptors() {
        IFacetDescriptorManager fdm = getFacetContext().getWoko().getJFacets().getFacetRepository().getFacetDescriptorManager();
        FacetDescriptor[] descriptors = fdm.getDescriptors();
        return Arrays.asList(descriptors);
    }

}

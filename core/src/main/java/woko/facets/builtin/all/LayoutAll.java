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
import woko.facets.BaseFacet;
import woko.facets.builtin.Layout;
import woko.facets.builtin.WokoFacets;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <code>layout</code> facet for profile <code>all</code>.
 *
 * Assigned to profile <code>all</code> so that one can override for default <code>guest</code> easily without
 * using a custom default role (fallback profile).
 */
@FacetKey(name = WokoFacets.layout, profileId = "all")
public class LayoutAll<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseFacet<OsType, UmType, UnsType, FdmType> implements Layout {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/all/layout.jsp";

    /**
     * Return "Woko" (default app title)
     * @return the default "Woko" app title
     */
    public String getAppTitle() {
        return "Woko";
    }

    /**
     * Return an empty list (no specific CSS)
     * @return an empty list
     */
    public List<String> getCssIncludes() {
        return Collections.emptyList();
    }

    /**
     * Return an empty list (no specific JS)
     * @return an empty list
     */
    public List<String> getJsIncludes() {
        return Collections.emptyList();
    }

    /**
     * Return the path to the layout to be used for unauthenticated users
     * @return the path to the layout
     */
    public String getLayoutPath() {
        return FRAGMENT_PATH;
    }


}

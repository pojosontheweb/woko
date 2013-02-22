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
import woko.facets.BaseForwardResolutionFacet;
import woko.facets.BaseFragmentFacet;
import woko.facets.builtin.WokoFacets;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * The ThemeRoller is a tool which allow you to try the different themes provided by Woko (only with the bootstrap module)
 * This facet add the different themes in the navBar
 *
 * Available only to <code>developer</code> users by default. Override for your role(s) in
 * order to make this available for your users.
 */
@FacetKey(name = "themeRollerNavBar", profileId = "developer")
public class ThemeRollerNavBar<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseFragmentFacet<OsType,UmType,UnsType,FdmType> {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/developer/themeRollerNavBar.jsp";

    public String getPath() {
        return FRAGMENT_PATH;
    }

    public List<String> getAvailableThemes(){
        // TODO : get list of theme by parsing the theme's folder
        List<String> ret = new ArrayList<String>();
        ret.add("bootstrap-v2.3.0");
        ret.add("amelia");
        ret.add("superhero");

        return ret;
    }
}

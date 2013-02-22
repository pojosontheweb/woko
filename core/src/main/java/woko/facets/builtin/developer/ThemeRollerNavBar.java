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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String,String> getAvailableThemes(){
        Map<String, String> ret = new HashMap<String, String>();

        ret.put("Bootstrap", "bootstrap-v2.3.0");
        ret.put("Amelia", "amelia");
        ret.put("Cerulean", "cerulean");
        ret.put("Cosmo", "cosmo");
        ret.put("Cyborg", "cyborg");
        ret.put("Journal", "journal");
        ret.put("Readable", "readable");
        ret.put("Simplex", "simplex");
        ret.put("Slate", "slate");
        ret.put("SpaceLab", "spacelab");
        ret.put("Spruce", "spruce");
        ret.put("SuperHero", "superhero");
        ret.put("United", "united");

        return ret;
    }
}

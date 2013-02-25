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
import woko.Woko;
import woko.facets.BaseFragmentFacet;
import woko.facets.builtin.NavBar;
import woko.facets.builtin.WokoFacets;
import woko.facets.builtin.all.Link;
import woko.facets.builtin.all.NavBarAll;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <code>navBar</code> facet for <code>developer</code> role.
 */
@FacetKey(name = WokoFacets.navBar, profileId = "developer")
public class NavBarDev<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends NavBarAll<OsType,UmType,UnsType,FdmType>{

    public static <
            OsType extends ObjectStore,
            UmType extends UserManager,
            UnsType extends UsernameResolutionStrategy,
            FdmType extends IFacetDescriptorManager
            > List<Link> createDevLinks(Woko<OsType,UmType,UnsType,FdmType> woko, HttpServletRequest request) {
        ArrayList<Link> res = new ArrayList<Link>();
        res.add(new Link("/find", woko.getLocalizedMessage(request.getLocale(), "woko.devel.navbar.find")));
        res.add(new Link("/create", woko.getLocalizedMessage(request.getLocale(), "woko.devel.navbar.create")));
        res.add(new Link("/studio", woko.getLocalizedMessage(request.getLocale(), "woko.devel.navbar.studio")));
        return res;
    }

    private static List<Link> DEV_LINKS = null;

    @Override
    public List<Link> getLinks() {
        if (DEV_LINKS==null) {
            DEV_LINKS = createDevLinks(getWoko(), getRequest());
        }
        return DEV_LINKS;
    }
}

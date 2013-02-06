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
import woko.Woko;
import woko.facets.BaseFragmentFacet;
import woko.facets.WokoFacetContext;
import woko.facets.builtin.*;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;
import woko.util.Util;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Generic <code>renderLinks</code> facet for unauthenticated users. Tries to retrieve
 * <code>view</code> and <code>edit</code> facets in order to create the links
 * to be displayed for the target object.
 */
@FacetKey(name = WokoFacets.renderLinks, profileId = "all")
public class RenderLinksImpl<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseFragmentFacet<OsType,UmType,UnsType,FdmType> implements RenderLinks {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/all/renderLinks.jsp";

    public String getPath() {
        return FRAGMENT_PATH;
    }

    public List<Link> getLinks() {
        List<Link> links = new ArrayList<Link>();
        WokoFacetContext<OsType,UmType,UnsType,FdmType> facetContext = getFacetContext();
        Woko<OsType,UmType,UnsType,FdmType> woko = getFacetContext().getWoko();
        Object o = facetContext.getTargetObject();
        Class<?> oc = o.getClass();
        HttpServletRequest request = getFacetContext().getRequest();
        OsType store = woko.getObjectStore();
        Locale locale = request.getLocale();

        // display edit link if object can be edited (use instanceof because could be a login required facet)
        Object editFacet = woko.getFacet(WokoFacets.edit, request, o, oc);
        if (editFacet instanceof Edit) {
            String className = store.getClassMapping(oc);
            String key = store.getKey(o);
            if (key != null) {
                links.add(new Link(WokoFacets.edit + "/" + className + "/" + key, Util.getMessage(locale, "woko.links.edit")).setCssClass("link-edit"));
            }
        }

        Object deleteFacet = woko.getFacet(WokoFacets.delete, request, o, oc);
        if (deleteFacet instanceof Delete) {
            String className = store.getClassMapping(oc);
            String key = store.getKey(o);
            if (key != null) {
                links.add(new Link(WokoFacets.delete + "/" + className + "/" + key, Util.getMessage(locale, "woko.links.delete")).setCssClass("link-delete"));
            }
        }

        Object jsonFacet = woko.getFacet(WokoFacets.json, request, o, oc);
        if (jsonFacet instanceof Json) {
            String className = store.getClassMapping(oc);
            String key = store.getKey(o);
            if (key != null) {
                links.add(new Link(WokoFacets.json + "/" + className + "/" + key, Util.getMessage(locale, "woko.links.json")).setCssClass("link-json"));
            }
        }

        return links;
    }

}

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

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.Woko;
import woko.facets.BaseFragmentFacet;
import woko.facets.WokoFacetContext;
import woko.facets.builtin.*;
import woko.persistence.ObjectStore;
import woko.util.Util;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@FacetKey(name = WokoFacets.renderLinks, profileId = "all")
public class RenderLinksImpl extends BaseFragmentFacet implements RenderLinks {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/all/renderLinks.jsp";

    public String getPath() {
        return FRAGMENT_PATH;
    }

    public List<Link> getLinks() {
        List<Link> links = new ArrayList<Link>();
        WokoFacetContext facetContext = getFacetContext();
        Woko woko = getFacetContext().getWoko();
        Object o = facetContext.getTargetObject();
        Class<?> oc = o.getClass();
        HttpServletRequest request = getRequest();
        ObjectStore store = woko.getObjectStore();
        Locale locale = request.getLocale();

        // display edit link if object can be edited (use instanceof because could be a login required facet)
        Object editFacet = woko.getFacet(WokoFacets.edit, request, o, oc);
        if (editFacet instanceof Edit) {
            String className = store.getClassMapping(oc);
            String key = store.getKey(o);
            if (key != null) {
                links.add(new Link(WokoFacets.edit + "/" + className + "/" + key, Util.getMessage(locale, "woko.links.edit")).setCssClass("edit"));
            }
        }

        Object deleteFacet = woko.getFacet(WokoFacets.delete, request, o, oc);
        if (deleteFacet instanceof Delete) {
            String className = store.getClassMapping(oc);
            String key = store.getKey(o);
            if (key != null) {
                links.add(new Link(WokoFacets.delete + "/" + className + "/" + key, Util.getMessage(locale, "woko.links.delete")).setCssClass("delete"));
            }
        }

        Object jsonFacet = woko.getFacet(WokoFacets.json, request, o, oc);
        if (jsonFacet instanceof Json) {
            String className = store.getClassMapping(oc);
            String key = store.getKey(o);
            if (key != null) {
                links.add(new Link(WokoFacets.json + "/" + className + "/" + key, Util.getMessage(locale, "woko.links.json")).setCssClass("json"));
            }
        }

        return links;
    }

}

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
import woko.facets.builtin.Delete;
import woko.facets.builtin.RenderLinks;
import woko.facets.builtin.View;
import woko.facets.builtin.WokoFacets;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;
import woko.util.Util;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Generic <code>renderLinksEdit</code> facet for unauthenticated users. Tries to retrieve
 * <code>view</code>, <code>delete</code> and other CRUD resolution facets in order to create the links
 * to be displayed for the target object.
 */
@FacetKey(name = WokoFacets.renderLinksEdit, profileId = "all")
public class RenderLinksEditImpl<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends RenderLinksImpl<OsType,UmType,UnsType,FdmType> {

    @Override
    public List<Link> getLinks() {
        List<Link> links = new ArrayList<Link>();
        WokoFacetContext<OsType,UmType,UnsType,FdmType> facetContext = getFacetContext();
        Woko<OsType,UmType,UnsType,FdmType> woko = getFacetContext().getWoko();
        Object o = facetContext.getTargetObject();
        if (o!=null) {
            OsType store = woko.getObjectStore();
            Class<?> oc = store.getObjectClass(o);
            HttpServletRequest request = getRequest();
            Locale locale = request.getLocale();

            // display view link if object can be displayed
            Object viewFacet = woko.getFacet(WokoFacets.view, request, o, oc);
            if (viewFacet instanceof View) {
                String className = store.getClassMapping(oc);
                String key = store.getKey(o);
                if (key != null) {
                    links.add(new Link(WokoFacets.view + "/" + className + "/" + key, Util.getMessage(locale, "woko.links.close.editing")).setCssClass("link-close"));
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
        }
        return links;
    }

}

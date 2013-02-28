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
import net.sourceforge.stripes.util.ReflectUtil;
import woko.facets.BaseFragmentFacet;
import woko.facets.WokoFacetContext;
import woko.facets.builtin.ListObjects;
import woko.facets.builtin.RenderListTitle;
import woko.facets.builtin.RenderTitle;
import woko.facets.builtin.WokoFacets;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;
import woko.util.Util;
import woko.util.WLogger;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Generic <code>renderTitle</code> facet, assigned to <code>Object</code>.
 *
 * Looks for properties that could be used to render the title :
 * <ul>
 *     <li>title</li>
 *     <li>name</li>
 *     <li>id</li>
 *     <li>_id</li>
 * </ul>
 *
 * Defaults to <code>toString()</code> on the target object if no candidate property
 * could be found.
 */
@FacetKey(name = WokoFacets.renderListTitle, profileId = "all")
public class RenderListTitleImpl<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseFragmentFacet<OsType,UmType,UnsType,FdmType> implements RenderListTitle {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/all/renderListTitle.jsp";

    public String getPath() {
        return FRAGMENT_PATH;
    }

    public String getTitle() {
        ListObjects listFacet = (ListObjects)getRequest().getAttribute(WokoFacets.list);
        if (listFacet == null){
            throw new IllegalStateException("You are trying to get the listTitle outside a list facet");
        }

        Locale locale = getRequest().getLocale();
        return listFacet.getResults().getTotalSize() + " "
                + Util.getMessage(locale, "woko.devel.list.title") + " " +
                listFacet.getClassName();
    }

}

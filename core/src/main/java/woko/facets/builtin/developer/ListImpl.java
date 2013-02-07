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
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import woko.facets.WokoFacetContext;
import woko.facets.builtin.BaseResultFacet;
import woko.facets.builtin.RenderObjectJson;
import woko.facets.builtin.WokoFacets;
import woko.persistence.ListResultIterator;
import woko.persistence.ObjectStore;
import woko.persistence.ResultIterator;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

/**
 * Generic <code>list</code> resolution facet.
 *
 * Available only to <code>developer</code> users by default. Override for your role(s) in
 * order to make this available for your users.
 */
@FacetKey(name = WokoFacets.list, profileId = "developer")
public class ListImpl<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseResultFacet<OsType,UmType,UnsType,FdmType> implements woko.facets.builtin.ListObjects {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/developer/list.jsp";

    public String getPath() {
        return FRAGMENT_PATH;
    }

    protected ResultIterator<?> createResultIterator(ActionBeanContext abc, int start, int limit) {
        String className = getClassName();
        if (className == null) {
            return new ListResultIterator<Object>(Collections.emptyList(), start, limit, 0);
        } else {
            return getFacetContext().getWoko().getObjectStore().list(className, start, limit);
        }
    }
}

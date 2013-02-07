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
import woko.facets.builtin.BaseResultFacet;
import woko.facets.builtin.Search;
import woko.facets.builtin.WokoFacets;
import woko.persistence.ListResultIterator;
import woko.persistence.ObjectStore;
import woko.persistence.ResultIterator;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import java.util.Collections;

/**
 * <code>search</code> resolution facet : performs a full text search and displays the results.
 *
 * Available only to <code>developer</code> users by default. Override for your role(s) in
 * order to make this available for your users.
 */
@FacetKey(name = WokoFacets.search, profileId = "developer")
public class SearchImpl<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseResultFacet<OsType,UmType,UnsType,FdmType> implements Search {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/developer/search.jsp";

    /**
     * The full-text search query
     */
    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getPath() {
        return FRAGMENT_PATH;
    }

    /**
     * Perform the search and return a <code>ResultIterator</code>
     * @param abc the action bean context
     * @param start the start index (>=0)
     * @param limit the limit (-1 for no limit)
     *
     * @return a ResultIterator with the search results
     */
    protected ResultIterator<?> createResultIterator(ActionBeanContext abc, int start, int limit) {
        if (query == null) {
            return new ListResultIterator<Object>(Collections.emptyList(), start, limit, 0);
        } else {
            return getFacetContext().getWoko().getObjectStore().search(query, start, limit);
        }
    }


}

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

package woko.facets.builtin;

import woko.persistence.ResultIterator;

import java.util.Map;

/**
 * Base interface for facets that handle a paginated list of results (like
 * <code>list</code> and <code>search</code>).
 */
public interface ResultFacet<T> {

    /**
     * Return the results
     * @return the results
     */
    ResultIterator<T> getResults();

    /**
     * Return the currently displayed page
     * @return the current page
     */
    Integer getPage();

    /**
     * Return the number of results to be displayed per page
     * @return the number of results per page
     */
    Integer getResultsPerPage();

    /**
     * Return the title for the page
     * @deprecated Woko use now {@link woko.facets.builtin.RenderListTitle} to render the lists title
     * @return the title for the page
     */
    @Deprecated
    String getPageHeaderTitle();

    /**
     * Return a map of arguments used to pass arguments in paginated ResultFacet.
     *
     * @return the map of arguments
     */
    Map<String, Object> getArgs();
}
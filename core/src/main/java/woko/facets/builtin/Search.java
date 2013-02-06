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

import woko.facets.ResolutionFacet;

/**
 * <code>search</code> resolution facets allow to search for Woko managed POJOs in a full text fashion.
 */
public interface Search extends ResolutionFacet, ResultFacet {

    static final String FACET_NAME = "search";

    /**
     * Return the query supplied for the search
     * @return the query supplied for the search
     */
    String getQuery();

}

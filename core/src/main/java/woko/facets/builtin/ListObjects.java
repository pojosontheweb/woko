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
 * <code>list</code> facets allows to browse instances of a given type, via URLs like :
 *
 * <pre>http://.../list/MyClass</pre>
 *
 * The target type of the facet is used in order to query for instances of a given class. The resulting
 * page displays all instances of that target type, with pagination.
 *
 * You can override <code>list</code> completely in order to design your own page from scratch, or
 * override <code>renderListItem</code> in order to customize the rendering of the items in the list.
 */
public interface ListObjects extends ResolutionFacet, ResultFacet {

    static final String FACET_NAME = "list";

    /**
     * Return the class name we want to list instances for. Should return the target object type
     * of the facet.
     * @return the target class name
     */
    String getClassName();

    /**
     * Return an optional CSS class to be used for the list wrapper
     * @return an optional CSS class to be used for the list wrapper
     */
    String getListWrapperCssClass();

}

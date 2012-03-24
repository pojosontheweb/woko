/*
 * Copyright 2001-2010 Remi Vankeisbelck
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

package facets

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.BaseResolutionFacet
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.StreamingResolution

@FacetKey(name="deleteTestObjects", profileId="developer")
class DeleteTestObjects extends BaseResolutionFacet {

    Resolution getResolution(ActionBeanContext abc) {
        def s = facetContext.woko.objectStore
        def allBooks = s.list('Book', 0, null)
        while (allBooks.hasNext()) {
            s.delete(allBooks.next())
        }
        new StreamingResolution('text/json', "{success:true}")
    }


}

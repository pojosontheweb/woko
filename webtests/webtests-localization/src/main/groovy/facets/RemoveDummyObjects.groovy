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

package facets

import net.sourceforge.jfacets.annotations.FacetKey
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.StreamingResolution
import woko.facets.BaseResolutionFacet

@FacetKey(name="removeDummyObjects", profileId="all")
class RemoveDummyObjects extends BaseResolutionFacet {

    Resolution getResolution(ActionBeanContext abc) {
        def store = facetContext.woko.objectStore
        for (int i=10 ; i<60 ; i++) {
            def b = store.load('MyBook', "$i")
            store.delete(b)
        }
        return new StreamingResolution("text/plain", "all good")
    }


}

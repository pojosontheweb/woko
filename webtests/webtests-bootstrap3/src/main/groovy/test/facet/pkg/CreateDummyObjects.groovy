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

package test.facet.pkg

import net.sourceforge.jfacets.annotations.FacetKey
import test.MyEntity
import woko.facets.BaseResolutionFacet
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext
import test.MyBook
import net.sourceforge.stripes.action.StreamingResolution

@FacetKey(name="createDummyObjects", profileId="all")
class CreateDummyObjects extends BaseResolutionFacet {

    Resolution getResolution(ActionBeanContext abc) {
        def store = facetContext.woko.objectStore
        for (int i=100 ; i<500 ; i++) {
            store.save(new MyBook([name:"Moby test$i", _id:i]))
        }
        for (int i=100 ; i<200 ; i++) {
            store.save(new MyEntity([id:Long.valueOf(i), prop1:'blah blah', prop2:120]))
        }
        return new StreamingResolution("text/plain", "all good")
    }


}

package facets

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.BaseResolutionFacet
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext
import test.MyBook
import net.sourceforge.stripes.action.StreamingResolution

@FacetKey(name="createDummyObjects", profileId="all")
class CreateDummyObjects extends BaseResolutionFacet {

    Resolution getResolution() {
        def store = facetContext.woko.objectStore
        for (int i=100 ; i<500 ; i++) {
            store.save(new MyBook([name:"Moby test$i", _id:i]))
        }
        return new StreamingResolution("text/plain", "all good")
    }


}

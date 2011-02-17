package facets

import net.sourceforge.jfacets.annotations.FacetKey
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.StreamingResolution
import test.MyBook
import woko.facets.BaseResolutionFacet

@FacetKey(name="createDummyObjects", profileId="all")
class CreateDummyObjects extends BaseResolutionFacet {

    Resolution getResolution(ActionBeanContext abc) {
        def store = facetContext.woko.objectStore
        for (int i=10 ; i<60 ; i++) {
            store.save(new MyBook([name:"Moby test$i", _id:i]))
        }
        return new StreamingResolution("text/plain", "all good")
    }


}

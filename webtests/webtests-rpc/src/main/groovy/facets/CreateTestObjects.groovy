package facets

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.BaseResolutionFacet
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.StreamingResolution
import test.MyBook

@FacetKey(name="createTestObjects", profileId="developer")
class CreateTestObjects extends BaseResolutionFacet {

    Resolution getResolution(ActionBeanContext abc) {
        def store = facetContext.woko.objectStore
        for (int i=0;i<100;i++) {
            MyBook book = new MyBook([_id: i, name: "Moby$i", nbPages: 123])
            store.save(book)
        }
        new StreamingResolution('text/plain', 'OK')
    }


}

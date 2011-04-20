package facets

import net.sourceforge.jfacets.annotations.FacetKey
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.StreamingResolution
import woko.facets.BaseResolutionFacet

@FacetKey(name="removeDummyObjects", profileId="all")
class RemoveDummyObjects extends BaseResolutionFacet {

    Resolution getResolution() {
        def store = facetContext.woko.objectStore
        for (int i=100 ; i<500 ; i++) {
            def b = store.load('MyBook', "$i")
            store.delete(b)
        }
        return new StreamingResolution("text/plain", "all good")
    }


}

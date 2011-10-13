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
        new StreamingResolution('text/plain', 'OK')
    }


}

package facets

import entities.MyEntity
import net.sourceforge.jfacets.annotations.FacetKey
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.Resolution
import woko.facets.BaseResolutionFacet
import woko.persistence.ObjectStore

@FacetKey(name = "saveAndThrow", profileId = "developer")
class SaveAndThrow extends BaseResolutionFacet {

    Resolution getResolution(ActionBeanContext abc) {
        ObjectStore s = woko.objectStore
        MyEntity e = s.load("MyEntity", "1")
        e.name = "bar"
        s.save(e)

        throw new RuntimeException("boom")
    }
}

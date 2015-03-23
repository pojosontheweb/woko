package facets

import net.sourceforge.jfacets.annotations.FacetKey
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.StreamingResolution
import woko.facets.BaseResolutionFacet
import woko.inmemory.InMemoryObjectStore
import woko.ioc.WokoInject
import woko.ioc.WokoIocContainer

@FacetKey(name="testInjectOnFieldAutoWire", profileId = "developer")
class TestInjectOnFieldAutoWire extends BaseResolutionFacet {

    @WokoInject
    InMemoryObjectStore myStore

    Resolution getResolution(ActionBeanContext abc) {
        if (myStore) {
            return new StreamingResolution("text/plain", "injected")
        } else {
            return new StreamingResolution("text/plain", "FAILED")
        }
    }
}

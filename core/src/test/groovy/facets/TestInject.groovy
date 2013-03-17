package facets

import net.sourceforge.jfacets.annotations.FacetKey
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.StreamingResolution
import woko.facets.BaseResolutionFacet
import woko.inmemory.InMemoryObjectStore
import woko.ioc.WokoInject
import woko.ioc.WokoIocContainer
import woko.persistence.ObjectStore

@FacetKey(name="testInject", profileId = "developer")
class TestInject extends BaseResolutionFacet {

    private InMemoryObjectStore injectedObjectStore = null

    @WokoInject(WokoIocContainer.ObjectStore)
    void injectObjectStore(InMemoryObjectStore objectStore) {
        injectedObjectStore = objectStore
    }

    Resolution getResolution(ActionBeanContext abc) {
        if (injectedObjectStore) {
            return new StreamingResolution("text/plain", "injected")
        } else {
            return new StreamingResolution("text/plain", "FAILED")
        }
    }
}

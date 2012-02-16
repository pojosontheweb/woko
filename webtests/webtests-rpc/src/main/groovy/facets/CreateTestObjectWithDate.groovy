package facets

import woko.facets.BaseResolutionFacet
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext
import test.MyBook
import net.sourceforge.stripes.action.StreamingResolution
import net.sourceforge.jfacets.annotations.FacetKey

@FacetKey(name="createTowd", profileId="all")
class CreateTestObjectWithDate extends BaseResolutionFacet {

    Resolution getResolution(ActionBeanContext abc) {
        MyBook b = new MyBook([_id: 332211, name: "foobarbaz", creationTime: new Date()])
        facetContext.woko.objectStore.save(b)
        return new StreamingResolution("text/json", "{success:true}")
    }


}

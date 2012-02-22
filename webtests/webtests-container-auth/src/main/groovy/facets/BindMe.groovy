package facets

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.BaseForwardResolutionFacet
import test.OtherPojo
import net.sourceforge.stripes.action.StrictBinding

@StrictBinding(deny=[
        "neverBound",
        "other.foo"
])
@FacetKey(name="bindMe", profileId="all")
class BindMe extends BaseForwardResolutionFacet {

    String alwaysBound

    String neverBound = "shouldnotchange"

    OtherPojo other  = new OtherPojo()

    @Override
    String getPath() {
        "/WEB-INF/jsp/bindMe.jsp"
    }


}

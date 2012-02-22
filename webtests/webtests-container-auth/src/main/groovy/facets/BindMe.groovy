package facets

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.BaseForwardResolutionFacet
import test.OtherPojo
import net.sourceforge.stripes.action.StrictBinding
import net.sourceforge.stripes.action.StrictBinding.Policy
import test.MyEntity

@StrictBinding(
    defaultPolicy=Policy.ALLOW,
    deny=[
        "facet.neverBound",
        "facet.other.foo"
    ]
)
@FacetKey(name="bindMe", profileId="all", targetObjectType=MyEntity.class)
class BindMe extends BaseForwardResolutionFacet {

    String alwaysBound

    String neverBound = "shouldnotchange"

    OtherPojo other  = new OtherPojo()

    @Override
    String getPath() {
        "/WEB-INF/jsp/bindMe.jsp"
    }


}

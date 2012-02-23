package facets

import net.sourceforge.stripes.action.StrictBinding.Policy
import net.sourceforge.stripes.action.StrictBinding
import net.sourceforge.jfacets.annotations.FacetKey
import test.MyEntity
import woko.facets.builtin.WokoFacets
import woko.facets.builtin.developer.SaveImpl


@StrictBinding(
    defaultPolicy=Policy.ALLOW,
    deny=[
        "object.prop2"
    ]
)
@FacetKey(name=WokoFacets.save, profileId="developer", targetObjectType=MyEntity.class)
class SaveMyEntityDevel extends SaveImpl {
}

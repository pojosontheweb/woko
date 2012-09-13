package facets

import net.sourceforge.jfacets.annotations.FacetKey
import test.MyEntity
import woko.facets.builtin.developer.ViewImpl

@FacetKey(name="view", profileId="guest",targetObjectType=MyEntity.class)
class ViewMyEntityGuest extends ViewImpl{
}

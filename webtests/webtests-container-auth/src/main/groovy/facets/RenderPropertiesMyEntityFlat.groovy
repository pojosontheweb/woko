package facets

import test.MyEntity
import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.builtin.all.RenderPropertiesFlatLayout

@FacetKey(name="renderProperties", profileId="all", targetObjectType=MyEntity.class)
class RenderPropertiesMyEntityFlat extends RenderPropertiesFlatLayout {
}

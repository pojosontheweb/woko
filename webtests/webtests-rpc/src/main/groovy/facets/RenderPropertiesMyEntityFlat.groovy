package facets

import test.MyEntity
import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.builtin.all.RenderPropertiesImpl

@FacetKey(name="renderProperties", profileId="all", targetObjectType=MyEntity.class)
class RenderPropertiesMyEntityFlat extends RenderPropertiesImpl {

  RenderPropertiesMyEntityFlat() {
    useFlatLayout = true
  }
}

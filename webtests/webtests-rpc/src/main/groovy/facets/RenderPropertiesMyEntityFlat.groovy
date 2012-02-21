package facets

import test.MyEntity
import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.builtin.all.RenderPropertiesImpl
import woko.facets.builtin.WokoFacets

@FacetKey(name=WokoFacets.renderProperties, profileId="all", targetObjectType=MyEntity.class)
class RenderPropertiesMyEntityFlat extends RenderPropertiesImpl {

  RenderPropertiesMyEntityFlat() {
    useFlatLayout = true
  }
}

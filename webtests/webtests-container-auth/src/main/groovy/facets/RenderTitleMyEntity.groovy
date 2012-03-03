package facets

import woko.facets.builtin.all.RenderTitleImpl
import net.sourceforge.jfacets.annotations.FacetKey
import test.MyEntity

@FacetKey(name="renderTitle", profileId="all", targetObjectType=MyEntity.class)
class RenderTitleMyEntity extends RenderTitleImpl {

    @Override
    String getTitle() {
        return facetContext.targetObject?.prop1
    }


}

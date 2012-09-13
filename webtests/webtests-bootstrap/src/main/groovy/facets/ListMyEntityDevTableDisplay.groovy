package facets

import net.sourceforge.jfacets.annotations.FacetKey
import test.MyBook
import woko.facets.builtin.developer.ListImpl
import test.MyEntity
import woko.facets.builtin.developer.ListTabularImpl

@FacetKey(name="list", profileId="developer", targetObjectType=MyEntity.class)
class ListMyEntityDevTableDisplay extends ListTabularImpl {

}

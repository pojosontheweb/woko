package facets

import net.sourceforge.jfacets.annotations.FacetKey
import test.MyBook
import woko.facets.builtin.developer.ListImpl
import test.MyEntity

@FacetKey(name="list", profileId="developer", targetObjectType=MyEntity.class)
class ListMyEntityDevTableDisplay extends ListImpl {

    ListMyEntityDevTableDisplay() {
        setTableDisplay(true)
    }
}

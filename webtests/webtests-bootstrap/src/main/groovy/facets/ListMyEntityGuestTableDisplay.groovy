package facets

import net.sourceforge.jfacets.annotations.FacetKey
import test.MyEntity
import woko.facets.builtin.developer.ListTabularImpl

@FacetKey(name="list", profileId="guest") //, targetObjectType=MyEntity.class)
class ListMyEntityGuestTableDisplay extends ListTabularImpl {
    @Override
    List<String> getPropertyNames() {
        return ["id", "prop1", "prop2"]
    }

}

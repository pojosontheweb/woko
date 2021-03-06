package facets

import net.sourceforge.jfacets.annotations.FacetKey
import test.MyBook
import woko.facets.builtin.developer.ListImpl
import test.MyEntity
import woko.facets.builtin.developer.ListTabularImpl
import woko.persistence.ObjectStore
import woko.users.UserManager
import woko.users.RemoteUserStrategy
import woko.inmemory.InMemoryUserManager
import woko.hbcompass.HibernateCompassStore
import net.sourceforge.jfacets.annotations.AnnotatedFacetDescriptorManager

@FacetKey(name="list", profileId="developer", targetObjectType=MyEntity.class)
class ListMyEntityDevTableDisplay extends ListTabularImpl<HibernateCompassStore,InMemoryUserManager,RemoteUserStrategy,AnnotatedFacetDescriptorManager> {

    String dummyText = "Should be displayed only on load"

    @Override
    Map<String, Object> getArgs() {
        Map ret = new HashMap<String, Object>()
        ret.put('facet.dummyText', 'Should be displayed on each page except on load !')

        return ret
    }
}

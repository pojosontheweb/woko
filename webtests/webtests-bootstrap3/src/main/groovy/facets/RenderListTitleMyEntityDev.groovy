package facets

import net.sourceforge.jfacets.annotations.AnnotatedFacetDescriptorManager
import net.sourceforge.jfacets.annotations.FacetKey
import test.MyEntity
import woko.facets.builtin.RenderListTitle
import woko.facets.builtin.WokoFacets
import woko.facets.builtin.all.RenderListTitleImpl
import woko.facets.builtin.developer.ListTabularImpl
import woko.hbcompass.HibernateCompassStore
import woko.inmemory.InMemoryUserManager
import woko.users.RemoteUserStrategy

@FacetKey(name=WokoFacets.renderListTitle, profileId="developer", targetObjectType=MyEntity.class)
class RenderListTitleMyEntityDev extends RenderListTitleImpl<HibernateCompassStore, InMemoryUserManager, RemoteUserStrategy, AnnotatedFacetDescriptorManager> {

    @Override
    String getTitle() {
        return "TestPageHeaderTitleOverride"
    }

    @Override
    String getPath() {
        return '/WEB-INF/jsp/renderListTitleMyEntityDev.jsp'
    }
}

package woko.facets.builtin.all;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.BaseFragmentFacet;
import woko.facets.builtin.RenderListItem;
import woko.facets.builtin.WokoFacets;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

@FacetKey(name=WokoFacets.renderListItem, profileId="all")
public class RenderListItemImpl<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseFragmentFacet<OsType,UmType,UnsType,FdmType> implements RenderListItem {

    @Override
    public String getPath() {
        return "/WEB-INF/woko/jsp/all/renderListItem.jsp";
    }

    @Override
    public String getItemWrapperCssClass() {
        return null;
    }
}

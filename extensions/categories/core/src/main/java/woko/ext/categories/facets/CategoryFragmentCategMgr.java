package woko.ext.categories.facets;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.ext.categories.Category;
import woko.facets.BaseFragmentFacet;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

@FacetKey(name = "fragment", profileId = "categorymanager", targetObjectType = Category.class)
public class CategoryFragmentCategMgr<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        >  extends BaseFragmentFacet<OsType, UmType, UnsType, FdmType> {

    @Override
    public String getPath() {
        return "/WEB-INF/woko/ext/categories/categoryFragment.jsp";
    }
}

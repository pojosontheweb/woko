package woko.ext.categories.facets;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.ext.categories.Category;
import woko.facets.builtin.all.BaseRenderPropertyValueEdit;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

@FacetKey(name="renderPropertyValueEdit_subCategories", profileId = "categorymanager", targetObjectType = Category.class)
public class RenderPropertyValueEditSubCategoriesCategMgr<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseRenderPropertyValueEdit<OsType,UmType,UnsType,FdmType> {

    @Override
    public String getPath() {
        return "/WEB-INF/woko/ext/categories/renderPropertyValueEditSubCategories.jsp";
    }

}

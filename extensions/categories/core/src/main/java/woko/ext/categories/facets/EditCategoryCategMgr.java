package woko.ext.categories.facets;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.ext.categories.Category;
import woko.facets.builtin.Edit;
import woko.facets.builtin.View;
import woko.facets.builtin.developer.EditImpl;
import woko.facets.builtin.developer.ViewImpl;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

@FacetKey(name = Edit.FACET_NAME, profileId = "categorymanager", targetObjectType = Category.class)
public class EditCategoryCategMgr<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends EditImpl<OsType,UmType,UnsType,FdmType> {
}

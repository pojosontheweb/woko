package woko.ext.categories.facets;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.ext.categories.Category;
import woko.facets.builtin.Save;
import woko.facets.builtin.View;
import woko.facets.builtin.developer.SaveImpl;
import woko.facets.builtin.developer.ViewImpl;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

@FacetKey(name = Save.FACET_NAME, profileId = "categorymanager", targetObjectType = Category.class)
public class SaveCategoryCategMgr<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends SaveImpl<OsType,UmType,UnsType,FdmType> {
}

package woko.ext.categories.facets;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.ext.categories.Category;
import woko.facets.builtin.RenderProperties;
import woko.facets.builtin.all.RenderPropertiesImpl;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import java.util.Arrays;
import java.util.List;

@FacetKey(name = RenderProperties.FACET_NAME, profileId = "categorymanager", targetObjectType = Category.class)
public class RenderCategoryPropertiesCategMgr<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends RenderPropertiesImpl<OsType,UmType,UnsType,FdmType> {

    @Override
    public List<String> getPropertyNames() {
        List<String> all = super.getPropertyNames();
        all.remove("id");
        all.remove("class");
        all.remove("sortIndex");
        all.remove("name");
        return all;
    }

}

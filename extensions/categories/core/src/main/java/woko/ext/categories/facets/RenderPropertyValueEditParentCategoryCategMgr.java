package woko.ext.categories.facets;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.ext.categories.Category;
import woko.facets.builtin.all.RenderPropertyValueEditXToOneRelation;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import java.util.ArrayList;
import java.util.List;

@FacetKey(name="renderPropertyValueEdit_parentCategory", profileId = "categorymanager", targetObjectType = Category.class)
public class RenderPropertyValueEditParentCategoryCategMgr<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends RenderPropertyValueEditXToOneRelation<OsType,UmType,UnsType,FdmType> {

    @Override
    public List<?> getChoices() {
        List<?> all = new ArrayList<Object>(super.getChoices());
        // remove the owning object : cannot set yourself as a parent !
        Category owningObject = (Category)getOwningObject();
        if (owningObject!=null) {
            all.remove(owningObject);
        }
        return all;
    }
}

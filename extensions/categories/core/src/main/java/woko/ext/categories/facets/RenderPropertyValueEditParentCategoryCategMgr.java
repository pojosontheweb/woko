package woko.ext.categories.facets;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.ext.categories.Category;
import woko.ext.categories.CategoryManager;
import woko.facets.builtin.all.RenderPropertyValueEditXToOneRelation;
import woko.ioc.WokoInject;
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

    private CategoryManager categoryManager;

    @WokoInject(CategoryManager.KEY)
    public void injectCm(CategoryManager cm) {
        this.categoryManager = cm;
    }

    /**
     * Filter the choices for assigning the parent category.
     * We don't allow setting a child as parent, as this wrecks the tree structure.
     * @return
     */
    @Override
    public List<?> getChoices() {
        Category owningObject = (Category)getOwningObject();
        @SuppressWarnings("unchecked")
        List<Category> defaultChoices = (List<Category>)super.getChoices();
        return categoryManager.getChoicesForParent(owningObject, defaultChoices);
    }


}

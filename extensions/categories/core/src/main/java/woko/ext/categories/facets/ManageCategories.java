package woko.ext.categories.facets;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StrictBinding;
import woko.ext.categories.Category;
import woko.ext.categories.CategoryManager;
import woko.facets.BaseResolutionFacet;
import woko.ioc.WokoInject;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import java.util.List;

@StrictBinding(

)
@FacetKey(name = "manageCategories", profileId = "categorymanager")
public class ManageCategories<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseResolutionFacet<OsType,UmType,UnsType,FdmType> {

    private CategoryManager categoryManager;

    @WokoInject(CategoryManager.KEY)
    public void setCategManager(CategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }

    @Override
    public Resolution getResolution(ActionBeanContext abc) {
        return new ForwardResolution("/WEB-INF/woko/ext/categories/manage.jsp");
    }

    public List<Category> getRootCategories() {
        return categoryManager.getRootCategories();
    }
}

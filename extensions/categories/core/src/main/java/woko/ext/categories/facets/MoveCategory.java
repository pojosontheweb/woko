package woko.ext.categories.facets;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import woko.ext.categories.Category;
import woko.ext.categories.CategoryManager;
import woko.facets.BaseResolutionFacet;
import woko.ioc.WokoInject;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

@StrictBinding(
        allow = {"facet.up", "facet.targetUrl"}
)
@FacetKey(name = "move", profileId = "categorymanager", targetObjectType = Category.class)
public class MoveCategory<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseResolutionFacet<OsType,UmType,UnsType,FdmType> {

    public static final String FACET_NAME = "move";

    @Validate(required = true)
    private Boolean up;

    private String targetUrl;

    public Boolean getUp() {
        return up;
    }

    public void setUp(Boolean up) {
        this.up = up;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    private CategoryManager categoryManager;

    @WokoInject(CategoryManager.KEY)
    public void injectCm(CategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }

    protected Category getCategory() {
        return (Category)getFacetContext().getTargetObject();
    }

    @Override
    public Resolution getResolution(ActionBeanContext abc) {
        if (categoryManager.moveCategory(getCategory(), up)) {
            return afterMove(abc);
        } else {
            return didntMove(abc);
        }
    }

    protected String getTargetUrlNullSafe() {
        String target = getTargetUrl();
        if (target==null) {
            target = "/manageCategories";
        }
        if (!target.startsWith("/")) {
            target = "/" + target;
        }
        return target;
    }

    protected Resolution didntMove(ActionBeanContext abc) {
        return new ForwardResolution(getTargetUrlNullSafe());
    }

    protected Resolution afterMove(ActionBeanContext abc) {
        abc.getMessages().add(new LocalizableMessage("woko.ext.categories.moved"));
        return new RedirectResolution(getTargetUrlNullSafe());
    }

    public boolean isMoveUpAllowed() {
        return categoryManager.isMoveUpAllowed(getCategory());
    }

    public boolean isMoveDownAllowed() {
        return categoryManager.isMoveDownAllowed(getCategory());
    }

}

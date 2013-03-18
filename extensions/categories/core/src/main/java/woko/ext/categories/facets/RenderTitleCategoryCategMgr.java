package woko.ext.categories.facets;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.ext.categories.Category;
import woko.facets.builtin.RenderTitle;
import woko.facets.builtin.all.RenderTitleImpl;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@FacetKey(name= RenderTitle.FACET_NAME, profileId = "categorymanager", targetObjectType = Category.class)
public class RenderTitleCategoryCategMgr<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends RenderTitleImpl<OsType,UmType,UnsType,FdmType> {

    @Override
    public String getPath() {
        return "/WEB-INF/woko/ext/categories/renderTitleCategory.jsp";
    }

    public List<Category> getFullPath() {
        ArrayList<Category> res = new ArrayList<Category>();
        Category c = (Category)getFacetContext().getTargetObject();
        Category cycleDetect = c;
        while (c!=null) {
            res.add(c);
            c = c.getParentCategory();
            if (c!=null && c.equals(cycleDetect)) {
                break;
            }
        }
        Collections.reverse(res);
        return res;
    }


    public String getPathSeparator() {
        return "/";
    }
}

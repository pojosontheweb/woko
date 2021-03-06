package woko.ext.tinymce;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import woko.facets.builtin.Layout;
import woko.facets.builtin.all.BaseRenderPropertyValueEdit;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public abstract class BaseRenderPropertyValueEditTinyMCE<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseRenderPropertyValueEdit<OsType,UmType,UnsType,FdmType> {

    public String getTextAreaId() {
        return UUID.randomUUID().toString();
    }

    public String getContentCss() {
        HttpServletRequest request = getRequest();
        Layout layout = (Layout)request.getAttribute(Layout.FACET_NAME);
        if (layout==null) {
            layout = getWoko().getFacet(Layout.FACET_NAME, request, getFacetContext().getTargetObject());
        }
        String contentCss = "";
        if (layout!=null) {
            List<String> cssIncludes = layout.getCssIncludes();
            String contextPath = request.getContextPath();
            if (cssIncludes!=null) {
                for (Iterator<String> it = cssIncludes.iterator(); it.hasNext(); ) {
                    contentCss += contextPath + it.next();
                    if (it.hasNext()) {
                        contentCss += ",";
                    }

                }
            }
        }
        return contentCss;
    }

}

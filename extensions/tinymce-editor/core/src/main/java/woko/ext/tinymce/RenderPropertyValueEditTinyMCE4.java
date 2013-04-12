package woko.ext.tinymce;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import woko.facets.builtin.Layout;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;

public class RenderPropertyValueEditTinyMCE4<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends RenderPropertyValueEditTinyMCEBase<OsType,UmType,UnsType,FdmType> {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/tinymce/renderPropertyValueEditTinyMCE4.jsp";

    public String getPath() {
        return FRAGMENT_PATH;
    }

}

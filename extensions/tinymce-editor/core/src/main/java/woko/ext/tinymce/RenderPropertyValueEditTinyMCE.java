package woko.ext.tinymce;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import woko.facets.builtin.all.BaseRenderPropertyValueEdit;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import java.util.UUID;

public class RenderPropertyValueEditTinyMCE<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseRenderPropertyValueEdit<OsType,UmType,UnsType,FdmType> {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/tinymce/renderPropertyValueEditTinyMCE.jsp";

    public String getPath() {
        return FRAGMENT_PATH;
    }

    public String getTextAreaId() {
        return UUID.randomUUID().toString();
    }

}

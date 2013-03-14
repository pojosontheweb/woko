package woko.ext.raptor;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import woko.facets.builtin.all.BaseRenderPropertyValueEdit;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

public class RenderPropertyValueEditTinyMCE<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseRenderPropertyValueEdit<OsType,UmType,UnsType,FdmType> {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/raptor/renderPropertyValueEditTinyMCE.jsp";

    public String getPath() {
        return FRAGMENT_PATH;
    }

}

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

public abstract class RenderPropertyValueEditTinyMCEBase<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseRenderPropertyValueEdit<OsType,UmType,UnsType,FdmType> {

    public String getTextAreaId() {
        return UUID.randomUUID().toString();
    }

}

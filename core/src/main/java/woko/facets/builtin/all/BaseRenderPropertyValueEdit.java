package woko.facets.builtin.all;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import woko.facets.builtin.RenderPropertiesEdit;
import woko.facets.builtin.RenderPropertyValueEdit;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

/**
 * Base class for <code>RenderPropertyValueEdit</code> facets.
 */
public class BaseRenderPropertyValueEdit<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends RenderPropertyValueImpl<OsType,UmType,UnsType,FdmType> implements RenderPropertyValueEdit {

    private String fieldPrefix = null;

    @Override
    public String getFieldPrefix() {
        if (fieldPrefix==null) {
            fieldPrefix = "object";
            // try to lookup for an editProperties facet in the request,
            // that could have an overriden field prefix to apply
            RenderPropertiesEdit renderPropertiesEdit = (RenderPropertiesEdit)getRequest().getAttribute(RenderPropertiesEdit.FACET_NAME);
            if (renderPropertiesEdit!=null) {
                fieldPrefix = renderPropertiesEdit.getFieldPrefix();
            }
        }
        return fieldPrefix;
    }
}

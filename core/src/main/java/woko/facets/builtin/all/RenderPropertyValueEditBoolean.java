package woko.facets.builtin.all;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.util.ReflectUtil;
import woko.facets.builtin.WokoFacets;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import java.beans.PropertyDescriptor;

/**
 * <code>renderPropertyValueEdit</code> facet for Boolean properties. Handles
 * object wrapper type <code>java.lang.Boolean</code> and raw type <code>boolean</code>.
 */
@FacetKey(name = WokoFacets.renderPropertyValueEdit, profileId = "all", targetObjectType = Boolean.class)
public class RenderPropertyValueEditBoolean<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseRenderPropertyValueEdit<OsType,UmType,UnsType,FdmType> {

    public static final String FRAGMENT_PATH_RAWTYPE = "/WEB-INF/woko/jsp/all/renderPropertyValueEditBooleanRaw.jsp";
    public static final String FRAGMENT_PATH_WRAPPER = "/WEB-INF/woko/jsp/all/renderPropertyValueEditBooleanWrapper.jsp";

    public String getPath() {
        // check the property type
        PropertyDescriptor pd = ReflectUtil.getPropertyDescriptor(getOwningObject().getClass(), getPropertyName());
        if (pd.getPropertyType().isPrimitive()) {
            return FRAGMENT_PATH_RAWTYPE;
        } else {
            return FRAGMENT_PATH_WRAPPER;
        }
    }


}

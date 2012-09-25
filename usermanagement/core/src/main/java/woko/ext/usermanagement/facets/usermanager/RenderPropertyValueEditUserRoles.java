package woko.ext.usermanagement.facets.usermanager;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.ext.usermanagement.core.User;
import woko.facets.builtin.RenderPropertyValueEdit;
import woko.facets.builtin.all.BaseRenderPropertyValueEdit;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import java.util.Iterator;
import java.util.List;

@FacetKey(name="renderPropertyValueEdit_roles", profileId="usermanager", targetObjectType = User.class)
public class RenderPropertyValueEditUserRoles<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseRenderPropertyValueEdit<OsType,UmType,UnsType,FdmType> implements RenderPropertyValueEdit {

    @Override
    public String getPath() {
        return "/WEB-INF/woko/ext/usermanagement/renderPropertyValueEditUserRoles.jsp";
    }

    public String getRolesStr() {
        User user = (User)getOwningObject();
        List<String> roles = user.getRoles();
        if (roles==null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Iterator<String> it = roles.iterator(); it.hasNext(); ) {
            String role = it.next();
            sb.append(role);
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}

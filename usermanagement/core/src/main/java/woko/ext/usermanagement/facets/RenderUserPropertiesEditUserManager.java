package woko.ext.usermanagement.facets;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.ext.usermanagement.core.User;
import woko.facets.builtin.WokoFacets;
import woko.facets.builtin.all.RenderPropertiesEditImpl;

import java.util.ArrayList;
import java.util.List;

@FacetKey(name= WokoFacets.renderPropertiesEdit, profileId = "usermanager", targetObjectType = User.class)
public class RenderUserPropertiesEditUserManager extends RenderPropertiesEditImpl {

    @Override
    public List<String> getPropertyNames() {
        List<String> all = super.getPropertyNames();
        ArrayList<String> a = new ArrayList<String>(all);
        a.remove("encodedPassword");
        a.remove("password");
        a.remove("id");
        a.remove("class");
        return a;
    }
}

package woko.ext.usermanagement.facets;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.ext.usermanagement.core.User;
import woko.facets.builtin.WokoFacets;
import woko.facets.builtin.all.RenderPropertiesImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@FacetKey(name= WokoFacets.renderProperties, profileId="all", targetObjectType = User.class)
public class RenderUserProperties extends RenderPropertiesImpl {

    @Override
    public List<String> getPropertyNames() {
        List<String> all = super.getPropertyNames();
        ArrayList<String> withoutPassword = new ArrayList<String>(all);
        withoutPassword.remove("password");
        return Collections.unmodifiableList(withoutPassword);
    }
}

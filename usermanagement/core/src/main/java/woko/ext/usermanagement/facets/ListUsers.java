package woko.ext.usermanagement.facets;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.Woko;
import woko.ext.usermanagement.core.User;
import woko.facets.builtin.ListObjects;
import woko.facets.builtin.developer.ListTabularImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@FacetKey(name= ListObjects.FACET_NAME, profileId="developer", targetObjectType = User.class)
public class ListUsers extends ListTabularImpl {

    private static final List<String> PROP_NAMES;

    static {
        ArrayList<String> a = new ArrayList<String>();
        a.add("id");
        a.add("username");
        a.add("roles");
        a.add("class");
        PROP_NAMES = Collections.unmodifiableList(a);
    }

    @Override
    public List<String> getPropertyNames() {
        return PROP_NAMES;
    }

    @Override
    public String getPageHeaderTitle() {
        Woko woko = Woko.getWoko(getRequest().getSession().getServletContext());
        return woko.getLocalizedMessage(getRequest(), "woko.ext.usermanagement.list.users.page.header");
    }
}

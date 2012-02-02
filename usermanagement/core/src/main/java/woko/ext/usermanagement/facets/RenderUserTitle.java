package woko.ext.usermanagement.facets;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.ext.usermanagement.core.User;
import woko.facets.builtin.all.RenderTitleImpl;

@FacetKey(name="renderTitle", profileId="all", targetObjectType = User.class)
public class RenderUserTitle extends RenderTitleImpl {

    @Override
    public String getTitle() {
        User u = (User)getFacetContext().getTargetObject();
        if (u==null) {
            throw new IllegalStateException("user is null, cannot invoke renderTitle facet on it.");
        }
        return u.getUsername();
    }
}

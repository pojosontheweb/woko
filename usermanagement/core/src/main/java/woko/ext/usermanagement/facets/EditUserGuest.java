package woko.ext.usermanagement.facets;

import net.sourceforge.jfacets.IInstanceFacet;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.Woko;
import woko.ext.usermanagement.core.User;
import woko.facets.builtin.developer.EditImpl;

@FacetKey(name="edit", profileId = "guest", targetObjectType = User.class)
public class EditUserGuest extends EditImpl implements IInstanceFacet {

    @Override
    public boolean matchesTargetObject(Object targetObject) {
        // matches only if there's no logged in user
        Woko woko = getFacetContext().getWoko();
        return woko.getUsername(getRequest()) == null;
    }


}

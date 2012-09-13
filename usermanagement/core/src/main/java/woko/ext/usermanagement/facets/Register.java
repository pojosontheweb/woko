package woko.ext.usermanagement.facets;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import woko.ext.usermanagement.core.DatabaseUserManager;
import woko.facets.BaseResolutionFacet;
import woko.persistence.ObjectStore;

@FacetKey(name="register", profileId = "guest")
public class Register extends BaseResolutionFacet {

    @Override
    public Resolution getResolution(ActionBeanContext abc) {
        DatabaseUserManager dbm = (DatabaseUserManager)getWoko().getUserManager();
        ObjectStore objectStore = getWoko().getObjectStore();
        String className = objectStore.getClassMapping(dbm.getUserClass());
        return new ForwardResolution("/save/" + className).addParameter("createTransient", "true");
    }
}

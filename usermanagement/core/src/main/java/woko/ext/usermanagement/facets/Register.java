package woko.ext.usermanagement.facets;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import woko.ext.usermanagement.core.DatabaseUserManager;
import woko.facets.BaseResolutionFacet;
import woko.persistence.ObjectStore;
import woko.users.UsernameResolutionStrategy;

@FacetKey(name="register", profileId = "guest")
public class Register<
        OsType extends ObjectStore,
        UmType extends DatabaseUserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseResolutionFacet<OsType,UmType,UnsType,FdmType> {

    @Override
    public Resolution getResolution(ActionBeanContext abc) {
        DatabaseUserManager dbm = getWoko().getUserManager();
        ObjectStore objectStore = getWoko().getObjectStore();
        String className = objectStore.getClassMapping(dbm.getUserClass());
        return new ForwardResolution("/save/" + className).addParameter("createTransient", "true");
    }
}

package woko.facets.builtin.developer;


import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import woko.facets.BaseResolutionFacet;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

public class Theme<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
> extends BaseResolutionFacet<OsType,UmType,UnsType,FdmType> {

    @Override
    public Resolution getResolution(ActionBeanContext abc) {

    }
}



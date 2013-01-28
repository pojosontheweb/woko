package woko.async.facets;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import woko.async.JobDetails;
import woko.facets.builtin.all.RenderTitleImpl;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

class RenderTitleJobDetailsDeveloper<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager> extends RenderTitleImpl<OsType, UmType, UnsType, FdmType> {

    @Override
    public String getTitle() {
        return ((JobDetails)getFacetContext().getTargetObject()).getJobUuid();
    }
}

package woko.async.facets;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.async.JobDetails;
import woko.facets.builtin.RenderTitle;
import woko.facets.builtin.all.RenderTitleImpl;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

/**
 * Render the <code>uuid</code> of the job.
 */
@FacetKey(name = RenderTitle.FACET_NAME, profileId = "developer", targetObjectType = JobDetails.class)
class RenderTitleJobDetailsDeveloper<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager> extends RenderTitleImpl<OsType, UmType, UnsType, FdmType> {

    @Override
    public String getTitle() {
        JobDetails details = (JobDetails)getFacetContext().getTargetObject();
        if (details!=null) {
            String uuid = details.getJobUuid();
            if (uuid==null) {
                return "No UUID";
            }
            return uuid;
        }
        return "Transient JobDetails";
    }
}

package woko.async.facets;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.IInstanceFacet;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.LocalizableError;
import woko.async.Job;
import woko.async.JobDetails;
import woko.async.JobManager;
import woko.facets.BaseResolutionFacet;
import woko.facets.builtin.View;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;
import woko.util.LinkUtil;

@FacetKey(name="kill", profileId = "developer", targetObjectType = JobDetails.class)
public class KillJobDeveloper<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseResolutionFacet<OsType,UmType,UnsType,FdmType> implements IInstanceFacet {

    @Override
    public Resolution getResolution(ActionBeanContext abc) {
        JobDetails jd = (JobDetails)getFacetContext().getTargetObject();
        JobManager jm = getJobManager();
        String uuid = jd.getJobUuid();
        Job job = jm.getRunningJob(uuid);
        String viewUrl = "/" + LinkUtil.getUrl(getWoko(), jd, View.FACET_NAME);
        if (job==null) {
            abc.getValidationErrors().addGlobalError(new LocalizableError("woko.ext.async.job.not.running", uuid));
            return new ForwardResolution(viewUrl);
        } else {
            job.kill();
            abc.getMessages().add(new LocalizableMessage("woko.ext.async.job.sent.kill.signal", uuid));
            return new RedirectResolution(viewUrl);
        }
    }

    private JobManager getJobManager() {
        return getWoko().getIoc().getComponent(JobManager.KEY);
    }

    @Override
    public boolean matchesTargetObject(Object targetObject) {
        JobDetails jd = (JobDetails)getFacetContext().getTargetObject();
        if (jd==null) {
            return false;
        }
        JobManager jm = getJobManager();
        return jm!=null;
    }
}

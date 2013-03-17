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
import woko.ioc.WokoInject;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;
import woko.util.LinkUtil;

/**
 * Kills the job associated to target {@link JobDetails} if any. Facet doesn't
 * match if target <code>JobDetails</code> is null or if there is no
 * {@link JobManager} in IOC.
 */
@FacetKey(name="kill", profileId = "developer", targetObjectType = JobDetails.class)
public class KillJobDeveloper<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseResolutionFacet<OsType,UmType,UnsType,FdmType> implements IInstanceFacet {

    private JobManager jobManager;

    @WokoInject(JobManager.KEY)
    public void injectJobManager(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    /**
     * Default handler : retrieves running {@link Job} if any, and invokes <code>kill()</code> on it.
     */
    @Override
    public Resolution getResolution(ActionBeanContext abc) {
        JobDetails jd = (JobDetails)getFacetContext().getTargetObject();
        String uuid = jd.getJobUuid();
        Job job = jobManager.getRunningJob(uuid);
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


    /**
     * Don't match if JobDetails is null or there is no JobManager found in IOC
     * @param targetObject
     * @return
     */
    @Override
    public boolean matchesTargetObject(Object targetObject) {
        JobDetails jd = (JobDetails)getFacetContext().getTargetObject();
        if (jd==null) {
            return false;
        }
        return jobManager!=null;
    }
}

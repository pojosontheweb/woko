package webtests.async

import net.sourceforge.jfacets.annotations.FacetKey
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.RedirectResolution
import net.sourceforge.stripes.action.Resolution
import woko.async.JobManager
import woko.facets.BaseResolutionFacet
import woko.ioc.WokoInject

@FacetKey(name="startJobs", profileId="developer")
class StartJobs extends BaseResolutionFacet {

    private JobManager jobManager

    @WokoInject(JobManager.KEY)
    void injectJobManager(JobManager jm) {
        this.jobManager = jm
    }

    Resolution getResolution(ActionBeanContext abc) {
        for (def i in 1..10) {
            jobManager.submit(new MyJob(i), [new MyJobDetailsListener(woko.objectStore) ])
        }
        return new RedirectResolution("/list/HbJobDetails")
    }
}

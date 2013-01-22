package webtests.async

import net.sourceforge.jfacets.annotations.FacetKey
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.RedirectResolution
import net.sourceforge.stripes.action.Resolution
import woko.async.JobManager
import woko.facets.BaseResolutionFacet

@FacetKey(name="startJobs", profileId="developer")
class StartJobs extends BaseResolutionFacet {

    Resolution getResolution(ActionBeanContext abc) {
        JobManager jobManager = woko.ioc.getComponent(JobManager.KEY)
        for (def i in 1..10) {
            jobManager.submit(new MyJob(i), [new MyJobDetailsListener(woko.objectStore) ])
        }
        return new RedirectResolution("/list/HbJobDetails")
    }
}

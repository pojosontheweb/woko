package webtests.async

import net.sourceforge.jfacets.annotations.FacetKey
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.RedirectResolution
import net.sourceforge.stripes.action.Resolution
import woko.async.JobManager
import woko.async.hibernate.HibernateJobDetailsListener
import woko.facets.BaseResolutionFacet

@FacetKey(name="startJobs", profileId="developer")
class StartJobs extends BaseResolutionFacet {



    Resolution getResolution(ActionBeanContext abc) {
        int lower = 100;
        int higher = 500;
        JobManager jobManager = woko.ioc.getComponent(JobManager.KEY)
        for (def i in 1..10) {
            int random = (int)(Math.random() * (higher-lower)) + lower;
            jobManager.submit(new MyJob(random), [new HibernateJobDetailsListener(woko.objectStore) ])
        }
        return new RedirectResolution("/list/HbJobDetails")
    }
}

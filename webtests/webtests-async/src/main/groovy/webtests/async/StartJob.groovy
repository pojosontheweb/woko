package webtests.async

import net.sourceforge.jfacets.annotations.FacetKey
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.RedirectResolution
import net.sourceforge.stripes.action.Resolution
import woko.async.JobManager
import woko.async.hibernate.HibernateJobDetailsListener
import woko.facets.BaseResolutionFacet

@FacetKey(name="startJob", profileId="developer")
class StartJob extends BaseResolutionFacet {

    Resolution getResolution(ActionBeanContext abc) {
        JobManager jobManager = woko.ioc.getComponent(JobManager.KEY)
        jobManager.submit(new MyJob(100), [new HibernateJobDetailsListener(woko.objectStore) ])
        return new RedirectResolution("/list/HbJobDetails")
    }
}

package woko.async.facets;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.async.JobDetails;
import woko.facets.builtin.RenderLinks;
import woko.facets.builtin.all.Link;
import woko.facets.builtin.all.RenderLinksImpl;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;
import woko.util.LinkUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds <code>kill</code> link for target {@link JobDetails} if killable.
 */
@FacetKey(name= RenderLinks.FACET_NAME,profileId = "developer",targetObjectType = JobDetails.class)
public class RenderLinksJobDetailsDeveloper<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends RenderLinksImpl<OsType,UmType,UnsType,FdmType>{

    @Override
    public List<Link> getLinks() {
        ObjectStore os = getObjectStore();
        JobDetails jd = (JobDetails)getFacetContext().getTargetObject();
        List<Link> links = new ArrayList<Link>(super.getLinks());
        if (jd.getKillTime()==null && jd.getEndTime()==null) {
            String url = LinkUtil.getUrl(getWoko(), jd, "kill");
            links.add(new Link(url, "kill"));
        }
        return links;
    }
}

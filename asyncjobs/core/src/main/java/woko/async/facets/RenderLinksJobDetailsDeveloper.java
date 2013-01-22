package woko.async.facets;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.async.JobDetails;
import woko.facets.builtin.RenderLinks;
import woko.facets.builtin.all.Link;
import woko.facets.builtin.all.RenderLinksImpl;
import woko.persistence.ObjectStore;
import woko.util.LinkUtil;

import java.util.ArrayList;
import java.util.List;

@FacetKey(name= RenderLinks.FACET_NAME,profileId = "developer",targetObjectType = JobDetails.class)
public class RenderLinksJobDetailsDeveloper extends RenderLinksImpl {

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

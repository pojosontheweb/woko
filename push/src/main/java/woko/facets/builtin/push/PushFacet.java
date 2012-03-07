package woko.facets.builtin.push;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.Woko;
import woko.facets.BaseResolutionFacet;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.StreamingResolution;
import woko.push.PushFacetDescriptorManager;
import woko.util.WLogger;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@FacetKey(name = "push", profileId = "developer")
public class PushFacet extends BaseResolutionFacet {

    private static final WLogger logger = WLogger.getLogger(PushFacet.class);

    private List<String> sources = new ArrayList<String>();

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public Resolution getResolution(ActionBeanContext abc) {
        StringBuilder sb = new StringBuilder();
        if (sources!=null) {
            Woko woko = getFacetContext().getWoko();
            IFacetDescriptorManager fdm = woko.getJFacets().getFacetRepository().getFacetDescriptorManager();
            if (!(fdm instanceof PushFacetDescriptorManager)) {
                String msg = "Trying to push but facet descriptor manager ain't a PushFacetDescriptorManager !";
                logger.error(msg);
                throw new IllegalStateException(msg);
            }
            PushFacetDescriptorManager pfdm = (PushFacetDescriptorManager)fdm;
            HttpServletRequest request = getFacetContext().getRequest();
            String username = woko.getUsername(request);
            String remoteHost = request.getRemoteHost();
            logger.warn("Push requested by user '" + username + "' from remote host '" + remoteHost + "'...");
            logger.info("Pushing " + sources.size() + " source files");
            pfdm.reload(sources);
            logger.warn("user '" + username + "' pushed from remote host '" + remoteHost + "'");
            sb.append("OK");
        } else {
            sb.append("ERROR : facet.sources is null !");
        }
        return new StreamingResolution("text/plain", sb.toString());
    }

}

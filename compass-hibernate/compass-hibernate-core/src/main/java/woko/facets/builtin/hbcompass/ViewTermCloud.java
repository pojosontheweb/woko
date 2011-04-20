package woko.facets.builtin.hbcompass;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import woko.facets.BaseResolutionFacet;
import woko.hbcompass.tagcloud.CompassCloud;

@FacetKey(name = "termCloud", profileId = "developer")
public class ViewTermCloud extends BaseResolutionFacet {

    public Resolution getResolution() {
        return new ForwardResolution("/WEB-INF/woko/jsp/hbcompass/term-cloud.jsp");
    }

    public CompassCloud getCloud() {
        return new CompassCloud();
    }


}

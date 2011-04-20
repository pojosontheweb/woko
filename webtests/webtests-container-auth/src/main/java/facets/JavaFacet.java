package facets;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import woko.facets.BaseResolutionFacet;

@FacetKey(name="javaFacet", profileId = "all")
public class JavaFacet extends BaseResolutionFacet {

  public Resolution getResolution() {
    return new StreamingResolution("text/plain", "ok");
  }
}

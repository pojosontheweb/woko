package woko.facets;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

public abstract class BaseForwardResolutionFacet extends BaseResolutionFacet {

  public abstract String getPath();

  public Resolution getResolution() {
    return new ForwardResolution(getPath());
  }


}

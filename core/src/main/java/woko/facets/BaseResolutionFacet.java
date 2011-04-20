package woko.facets;

import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import woko.actions.WokoActionBeanContext;

public abstract class BaseResolutionFacet extends BaseFacet implements ResolutionFacet {

  private WokoActionBeanContext context;

  @Override
  public WokoActionBeanContext getContext() {
    return context;
  }

  @Override
  public void setContext(ActionBeanContext context) {
    this.context = (WokoActionBeanContext)context;
  }

  @DefaultHandler
  public abstract Resolution getResolution();
}

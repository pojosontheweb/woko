package woko.actions;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;

public class BaseActionBean implements ActionBean {

  private WokoActionBeanContext context;

  public void setContext(ActionBeanContext context) {
    this.context = (WokoActionBeanContext)context;
  }

  public WokoActionBeanContext getContext() {
    return context;
  }

}

package woko.actions

import net.sourceforge.stripes.action.ActionBean
import net.sourceforge.stripes.action.ActionBeanContext

class BaseActionBean implements ActionBean {

  private WokoActionBeanContext context

  void setContext(ActionBeanContext context) {
    this.context = (WokoActionBeanContext)context
  }

  WokoActionBeanContext getContext() {
    return context
  }

}

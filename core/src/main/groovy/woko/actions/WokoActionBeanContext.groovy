package woko.actions

import woko.Woko
import net.sourceforge.stripes.action.ActionBeanContext

class WokoActionBeanContext extends ActionBeanContext {

  public Woko getWoko() {
    return Woko.getWoko(servletContext)
  }
  
}

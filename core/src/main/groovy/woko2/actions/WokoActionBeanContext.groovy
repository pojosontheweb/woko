package woko2.actions

import woko2.Woko
import net.sourceforge.stripes.action.ActionBeanContext

class WokoActionBeanContext extends ActionBeanContext {

  public Woko getWoko() {
    return Woko.getWoko(servletContext)
  }
  
}

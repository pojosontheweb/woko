package woko.actions;

import net.sourceforge.stripes.action.ActionBeanContext;
import woko.Woko;

public class WokoActionBeanContext extends ActionBeanContext {

  public Woko getWoko() {
    return Woko.getWoko(getServletContext());
  }

}
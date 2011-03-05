package woko.actions.nestedvalidation;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

@UrlBinding("my.action")
public class MyActionNotTyped implements ActionBean {

  private ActionBeanContext context;

  public ActionBeanContext getContext() {
    return context;
  }

  public void setContext(ActionBeanContext context) {
    this.context = context;
  }

  @ValidateNestedProperties({})
  private Object myPojo;

  public Object getMyPojo() {
    return myPojo;
  }

  public void setMyPojo(Object myPojo) {
    this.myPojo = myPojo;
  }
}

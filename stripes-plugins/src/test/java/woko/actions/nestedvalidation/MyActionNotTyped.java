package woko.actions.nestedvalidation;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

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

  @DefaultHandler
  public Resolution doIt() {
    return new ForwardResolution("/foo");
  }
}

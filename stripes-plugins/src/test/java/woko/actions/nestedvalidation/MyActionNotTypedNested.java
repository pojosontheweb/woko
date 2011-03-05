package woko.actions.nestedvalidation;


import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

public class MyActionNotTypedNested implements ActionBean {

  private ActionBeanContext context;

  public ActionBeanContext getContext() {
    return context;
  }

  public void setContext(ActionBeanContext context) {
    this.context = context;
  }

  @ValidateNestedProperties({})
  private Object myPojoNested;

  public Object getMyPojoNested() {
    return myPojoNested;
  }

  public void setMyPojoNested(Object o) {
    this.myPojoNested = o;
  }

  @DefaultHandler
  public Resolution doIt() {
    return new ForwardResolution("/foo");
  }
}

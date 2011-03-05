package woko.actions.nestedvalidation;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

public class MyActionTypedNested implements ActionBean {

  private ActionBeanContext context;

  public ActionBeanContext getContext() {
    return context;
  }

  public void setContext(ActionBeanContext context) {
    this.context = context;
  }

  @ValidateNestedProperties({})
  private MyPojoNested myPojoNested;

  public MyPojoNested getMyPojoNested() {
    return myPojoNested;
  }

  public void setMyPojoNested(MyPojoNested myPojoNested) {
    this.myPojoNested = myPojoNested;
  }

  @DefaultHandler
  public Resolution doIt() {
    return new ForwardResolution("/foo");
  }

}

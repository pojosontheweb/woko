package woko.actions.nestedvalidation;

import net.sourceforge.stripes.validation.ValidateNestedProperties;

public class MyPojoNested {

  @ValidateNestedProperties({})
  private MyPojo myPojo;

  public MyPojo getMyPojo() {
    return myPojo;
  }

  public void setMyPojo(MyPojo myPojo) {
    this.myPojo = myPojo;
  }

  private String otherProp;

  public String getOtherProp() {
    return otherProp;
  }

  public void setOtherProp(String otherProp) {
    this.otherProp = otherProp;
  }
}

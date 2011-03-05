package woko.actions.nestedvalidation;

import net.sourceforge.stripes.validation.Validate;

public class MyPojo {

  @Validate(required = true)
  private String prop;

  private String otherProp;

  public String getProp() {
    return prop;
  }

  public void setProp(String prop) {
    this.prop = prop;
  }

  public String getOtherProp() {
    return otherProp;
  }

  public void setOtherProp(String otherProp) {
    this.otherProp = otherProp;
  }
}

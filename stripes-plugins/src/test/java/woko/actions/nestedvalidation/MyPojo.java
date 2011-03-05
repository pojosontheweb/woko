package woko.actions.nestedvalidation;

import net.sourceforge.stripes.validation.Validate;

public class MyPojo {

  @Validate(required = true)
  private String prop;

  public String getProp() {
    return prop;
  }

  public void setProp(String prop) {
    this.prop = prop;
  }
}

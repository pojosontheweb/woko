package woko.actions

import net.sourceforge.stripes.action.UrlBinding
import net.sourceforge.stripes.action.ActionBean
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.ForwardResolution
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.DefaultHandler
import net.sourceforge.stripes.validation.Validate

@UrlBinding("/testValidate.action")
class TestActionBean implements ActionBean {

  ActionBeanContext context

  @Validate(required=true)
  String myProp

  @DefaultHandler
  Resolution doIt() {
    return new ForwardResolution("/")
  }


}

package woko2.facets.builtin

import net.sourceforge.stripes.action.ActionBeanContext

public interface Validate {

  static String name = 'validate'

  boolean validate(ActionBeanContext abc)

}
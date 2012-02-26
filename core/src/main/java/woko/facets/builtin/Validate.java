package woko.facets.builtin;

import net.sourceforge.stripes.action.ActionBeanContext;

public interface Validate {

    static final String FACET_NAME = "validate";

  boolean validate(ActionBeanContext abc);

}
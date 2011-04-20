package facets

import woko.facets.BaseResolutionFacet
import net.sourceforge.jfacets.annotations.FacetKey
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.ForwardResolution
import net.sourceforge.stripes.validation.Validate
import net.sourceforge.stripes.action.SimpleMessage

@FacetKey(name="testValidate", profileId="all")
class TestValidate extends BaseResolutionFacet {

  @Validate(required=true)
  String prop

  Resolution getResolution() {
    context.messages.add(new SimpleMessage("you have entered $prop"))
    return new ForwardResolution("/facet-validation-test.jsp")
  }


}

package facets

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.BaseForwardResolutionFacet
import net.sourceforge.stripes.validation.Validate

@FacetKey(name = "testMeToo", profileId = "all")
class TestWithNestedValidation extends BaseForwardResolutionFacet {

  @Validate(required = true)
  String myProp

  @Override
  String getPath() {
    return "${myProp}"
  }

}
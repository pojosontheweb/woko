package facets

import net.sourceforge.jfacets.annotations.FacetKey
import woko.inmemory.Book
import woko.facets.BaseForwardResolutionFacet

@FacetKey(name="testMe", profileId="all")
class TestWithEntityBinding extends BaseForwardResolutionFacet {

  Book book

  @Override
  String getPath() {
    return book?.name
  }


}

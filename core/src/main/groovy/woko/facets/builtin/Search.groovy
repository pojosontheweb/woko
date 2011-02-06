package woko.facets.builtin

import woko.facets.ResolutionFacet

interface Search extends ResolutionFacet, ResultFacet {

  static final String name = 'search'

  String getQuery()

}

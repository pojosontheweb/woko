package woko.facets.builtin

import woko.facets.ResolutionFacet

interface Search extends ResolutionFacet, ResultFacet {

  String getQuery()

}

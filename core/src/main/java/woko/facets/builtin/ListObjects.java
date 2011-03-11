package woko.facets.builtin

import woko.facets.ResolutionFacet

interface ListObjects extends ResolutionFacet, ResultFacet {

  String getClassName()

}

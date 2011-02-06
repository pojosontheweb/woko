package woko.facets.builtin

import woko.facets.ResolutionFacet

interface ListObjects extends ResolutionFacet, ResultFacet {

  static final String name = 'list'

  String getClassName()  

}

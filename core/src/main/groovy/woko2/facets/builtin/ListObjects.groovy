package woko2.facets.builtin

import woko2.facets.ResolutionFacet

interface ListObjects extends ResolutionFacet, ResultFacet {

  static final String name = 'list'

  String getClassName()  

}

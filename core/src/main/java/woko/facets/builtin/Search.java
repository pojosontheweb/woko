package woko.facets.builtin;

import woko.facets.ResolutionFacet;

public interface Search extends ResolutionFacet, ResultFacet {

  String getQuery();

}

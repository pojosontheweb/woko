package woko.facets.builtin;

import woko.facets.ResolutionFacet;

public interface Search extends ResolutionFacet, ResultFacet {

    static final String FACET_NAME = "search";

  String getQuery();

}

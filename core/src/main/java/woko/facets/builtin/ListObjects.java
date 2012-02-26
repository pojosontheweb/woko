package woko.facets.builtin;

import woko.facets.ResolutionFacet;

public interface ListObjects extends ResolutionFacet, ResultFacet {

    static final String FACET_NAME = "list";

  String getClassName();

}

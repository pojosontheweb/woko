package woko.facets.builtin;

import woko.facets.ResolutionFacet;

public interface Edit extends ResolutionFacet {

    static final String FACET_NAME = "edit";


  String getFragmentPath();

}
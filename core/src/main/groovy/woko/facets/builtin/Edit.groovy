package woko.facets.builtin

import woko.facets.ResolutionFacet

public interface Edit extends ResolutionFacet {

  static String name = 'edit'

  String getFragmentPath()

}
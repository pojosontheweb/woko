package woko2.facets.builtin

import woko2.facets.ResolutionFacet

public interface Edit extends ResolutionFacet {

  static String name = 'edit'

  String getFragmentPath()

}
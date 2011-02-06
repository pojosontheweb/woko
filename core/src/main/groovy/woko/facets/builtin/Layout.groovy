package woko.facets.builtin

import net.sourceforge.jfacets.IFacet

interface Layout extends IFacet {

  static String name = 'layout'

  String getAppTitle()

  List<String> getCssIncludes()

  List<String> getJsIncludes()

  String getLayoutPath()

}

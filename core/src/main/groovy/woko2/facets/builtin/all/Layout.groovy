package woko2.facets.builtin.all

import net.sourceforge.jfacets.IFacet

interface Layout extends IFacet {

  String getAppTitle()

  List<String> getCssIncludes()

  List<String> getJsIncludes()

  String getLayoutPath()

}

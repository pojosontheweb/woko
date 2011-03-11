package woko.facets.builtin;

import net.sourceforge.jfacets.IFacet;

import java.util.List;

public interface Layout extends IFacet {

  String getAppTitle();

  List<String> getCssIncludes();

  List<String> getJsIncludes();

  String getLayoutPath();

}

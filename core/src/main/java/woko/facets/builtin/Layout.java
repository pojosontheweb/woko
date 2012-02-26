package woko.facets.builtin;

import net.sourceforge.jfacets.IFacet;

import java.util.List;

public interface Layout extends IFacet {

    static final String FACET_NAME = "layout";

  String getAppTitle();

  List<String> getCssIncludes();

  List<String> getJsIncludes();

  String getLayoutPath();

}

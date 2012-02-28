package woko.facets.builtin.all;


import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.BaseFacet;
import woko.facets.builtin.Layout;
import woko.facets.builtin.WokoFacets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@FacetKey(name= WokoFacets.layout, profileId="all")
public class LayoutAll extends BaseFacet implements Layout {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/all/layout.jsp";

    public String getAppTitle() {
    return "Woko";
  }

  public List<String> getCssIncludes() {
    return Collections.emptyList();
  }

  public List<String> getJsIncludes() {
    return Collections.emptyList();
  }

  public String getLayoutPath() {
    return FRAGMENT_PATH;
  }


}

package woko.facets.builtin.all;


import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.BaseFacet;
import woko.facets.builtin.Layout;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@FacetKey(name="layout", profileId="all")
public class LayoutAll extends BaseFacet implements Layout {

  private static final List<String> CSS_INCLUDES =
      Arrays.asList("/woko/css/layout-all.css", "/woko/css/lithium/assets/style.css");

  public String getAppTitle() {
    return "Woko";
  }

  public List<String> getCssIncludes() {
    return CSS_INCLUDES;
  }

  public List<String> getJsIncludes() {
    return Collections.emptyList();
  }

  public String getLayoutPath() {
    return "/WEB-INF/woko/jsp/all/layout.jsp";
  }


}

package woko.facets.builtin.all;

import net.sourceforge.jfacets.IFacetContext;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.BaseFragmentFacet;
import woko.facets.builtin.RenderProperties;
import woko.util.Util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FacetKey(name="renderProperties", profileId="all")
public class RenderPropertiesFlatLayout extends RenderPropertiesImpl {

  @Override
  public String getPath() {
    return "/WEB-INF/woko/jsp/all/renderPropertiesFlatLayout.jsp";
  }
}

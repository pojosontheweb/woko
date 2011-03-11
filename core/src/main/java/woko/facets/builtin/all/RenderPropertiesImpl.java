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
public class RenderPropertiesImpl extends BaseFragmentFacet implements RenderProperties {

  private List<String> propertyNames;
  private Map<String,Object> propertyValues;

  public String getPath() {
    return "/WEB-INF/woko/jsp/all/renderProperties.jsp";
  }

  public List<String> getPropertyNames() {
    return propertyNames;
  }

  public Map<String, Object> getPropertyValues() {
    return propertyValues;
  }

  public void setFacetContext(IFacetContext iFacetContext) {
    super.setFacetContext(iFacetContext);
    Object obj = iFacetContext.getTargetObject();
    propertyNames = Util.getPropertyNames(obj, Arrays.asList("metaClass"));
    propertyValues = new HashMap<String,Object>();
    for (String pName : propertyNames) {
      propertyValues.put(pName, Util.getPropertyValue(obj, pName));
    }
  }

}

package facets;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.BaseFragmentFacet;
import woko.facets.builtin.RenderPropertyValue;

@FacetKey(name="renderPropertyValue_nbPages", profileId="all", targetObjectType=test.MyBook.class)
public class RenderPropertyValueMyBookNbPages extends BaseFragmentFacet implements RenderPropertyValue {

  private Object owningObject;
  private String propertyName;
  private Object propertyValue;

  public Object getOwningObject() {
    return owningObject;
  }

  public void setOwningObject(Object owningObject) {
    this.owningObject = owningObject;
  }

  public String getPropertyName() {
    return propertyName;
  }

  public void setPropertyName(String propertyName) {
    this.propertyName = propertyName;
  }

  public Object getPropertyValue() {
    return propertyValue;
  }

  public void setPropertyValue(Object propertyValue) {
    this.propertyValue = propertyValue;
  }

  public String getPath() {
    return "/WEB-INF/jsp/renderPropertyValueMyBookNbPages.jsp";
  }


}

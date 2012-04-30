/*
 * Copyright 2001-2012 Remi Vankeisbelck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

/*
 * Copyright 2001-2010 Remi Vankeisbelck
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

package test.facet.pkg

import javax.servlet.http.HttpServletRequest
import net.sourceforge.jfacets.annotations.FacetKey

import woko.facets.BaseFragmentFacet
import woko.facets.builtin.RenderPropertyValueEdit

@FacetKey(name='renderPropertyValueEdit_nbPages', profileId='all', targetObjectType=test.MyBook.class)
class RenderPropertyValueEditMyBookNbPages extends BaseFragmentFacet implements RenderPropertyValueEdit {

  Object owningObject
  String propertyName
  Object propertyValue

  String getPath() {
    return '/WEB-INF/jsp/renderPropertyValueEditMyBookNbPages.jsp'
  }


}

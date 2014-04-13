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

package facets

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.builtin.RenderPropertyValueJson
import woko.facets.BaseFacet
import javax.servlet.http.HttpServletRequest
import org.json.JSONObject

//@FacetKey(name='renderPropertyValueJson', profileId='all', targetObjectType=test.OtherPojo.class)
class RenderPropertyValueJsonOtherPojo extends BaseFacet implements RenderPropertyValueJson {

  def Object propertyToJson(HttpServletRequest request, Object propertyValue) {
    test.OtherPojo op = propertyValue
    JSONObject res = new JSONObject()
    res.put('name', op.foo)
  }


}

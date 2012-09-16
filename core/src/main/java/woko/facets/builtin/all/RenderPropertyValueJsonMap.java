/*
 * Copyright 2001-2012 Remi Vankeisbelck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import org.json.JSONObject;
import woko.Woko;
import woko.facets.BaseFacet;
import woko.facets.builtin.RenderPropertyValueJson;
import woko.facets.builtin.WokoFacets;
import woko.util.WLogger;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@FacetKey(name= WokoFacets.renderPropertyValueJson, profileId="all", targetObjectType=Map.class)
public class RenderPropertyValueJsonMap extends BaseFacet implements RenderPropertyValueJson {

  private static final WLogger logger = WLogger.getLogger(RenderPropertyValueJsonMap.class);

  public Object propertyToJson(HttpServletRequest request, Object propertyValue) {
      JSONObject result = new JSONObject();
      Map<?,?> map = (Map<?,?>)propertyValue;
      Woko<?,?,?,?> woko = getWoko();
      try {
          for (Object key : map.keySet()) {
              Object val = map.get(key);
              if (val!=null) {
                  RenderPropertyValueJson renderPropertyValueJson =
                                  (RenderPropertyValueJson)woko.getFacet(RenderPropertyValueJson.FACET_NAME, request, val);
                  Object propToJson = renderPropertyValueJson.propertyToJson(request, val);
                  result.put(key.toString(), propToJson);
              }

          }
          return result;
      } catch(Exception e) {
          throw new RuntimeException(e);
      }
  }

}

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

package woko.facets.builtin.developer;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import org.json.JSONObject;
import woko.facets.BaseFacet;
import woko.facets.WokoFacetContext;
import woko.facets.builtin.Json;
import woko.facets.builtin.RenderObjectJson;
import woko.facets.builtin.WokoFacets;

@FacetKey(name= WokoFacets.json, profileId="developer")
public class JsonImpl extends BaseFacet implements Json {

  public Resolution getResolution(ActionBeanContext abc) {
    WokoFacetContext facetContext = getFacetContext();
    RenderObjectJson roj =
        (RenderObjectJson)facetContext.getWoko().getFacet(WokoFacets.renderObjectJson, abc.getRequest(), facetContext.getTargetObject());
    JSONObject json = roj.objectToJson(abc.getRequest());
    String jsonStr = json.toString();
    return new StreamingResolution("text/json", jsonStr);
  }


}

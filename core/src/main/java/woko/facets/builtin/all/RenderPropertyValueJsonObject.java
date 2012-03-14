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

package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import org.json.JSONException;
import org.json.JSONObject;
import woko.Woko;
import woko.facets.BaseFacet;
import woko.facets.WokoFacetContext;
import woko.facets.builtin.RenderPropertyValueJson;
import woko.facets.builtin.RenderTitle;
import woko.facets.builtin.WokoFacets;
import woko.persistence.ObjectStore;

import javax.servlet.http.HttpServletRequest;

@FacetKey(name = WokoFacets.renderPropertyValueJson, profileId = "all")
public class RenderPropertyValueJsonObject extends BaseFacet implements RenderPropertyValueJson {

    public Object propertyToJson(HttpServletRequest request, Object propertyValue) {
        if (propertyValue == null) {
            return null;
        }
        JSONObject res = new JSONObject();
        RenderObjectJsonImpl.addWokoMetadata(getFacetContext().getWoko(), res, propertyValue, request);
        return res;
    }

}

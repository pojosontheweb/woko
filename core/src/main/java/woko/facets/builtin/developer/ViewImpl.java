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

package woko.facets.builtin.developer;

import net.sourceforge.jfacets.IInstanceFacet;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import woko.facets.BaseForwardResolutionFacet;
import woko.facets.BaseForwardRpcResolutionFacet;
import woko.facets.WokoFacetContext;
import woko.facets.builtin.Json;
import woko.facets.builtin.View;
import woko.facets.builtin.WokoFacets;

@FacetKey(name = WokoFacets.view, profileId = "developer")
public class ViewImpl extends BaseForwardRpcResolutionFacet implements View, IInstanceFacet {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/developer/view.jsp";

    public String getPath() {
        return FRAGMENT_PATH;
    }

    @Override
    protected Resolution getRpcResolution(ActionBeanContext abc) {
        WokoFacetContext wokoFacetContext = getFacetContext();
        Json json = (Json) wokoFacetContext.getWoko().getFacet(WokoFacets.json, wokoFacetContext.getRequest(), wokoFacetContext.getTargetObject());
        return json == null ? null : json.getResolution(abc);
    }

    @Override
    public boolean matchesTargetObject(Object targetObject) {
        return targetObject != null;
    }
}

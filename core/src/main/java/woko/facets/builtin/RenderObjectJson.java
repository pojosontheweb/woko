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

package woko.facets.builtin;

import org.json.JSONObject;
import net.sourceforge.jfacets.IFacet;

import javax.servlet.http.HttpServletRequest;

/**
 * <code>renderObjectJson</code> facets allow to transform a POJO into JSON. Woko includes a
 * generic implementation, so that any object can be converted to JSON for free.
 *
 * One can override this facet in order to customize the JSON representation of objects for the various
 * roles and classes of the app.
 */
public interface RenderObjectJson extends IFacet {

    static final String FACET_NAME = "renderObjectJson";

    /**
     * Return the facet's target object as JSON
     * @param request the request
     * @return a JSON representation of this facet's target object
     */
    JSONObject objectToJson(HttpServletRequest request);

}

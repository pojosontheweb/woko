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

import javax.servlet.http.HttpServletRequest;

/**
 * <code>renderPropertyValueJson</code> allows to convert an object property to JSON. It is used
 * by default by <code>renderObjectJson</code>, and can be overriden in order to customize the JSON
 * conversion of object properties for your classes and users.
 */
public interface RenderPropertyValueJson {

    static final String FACET_NAME = "renderPropertyValueJson";

    /**
     * Convert the property value to JSON. Can return basic types or JSONObject/JSONArray.
     * @param request the request
     * @param propertyValue the property value
     * @return the property as JSON
     */
    Object propertyToJson(HttpServletRequest request, Object propertyValue);

}
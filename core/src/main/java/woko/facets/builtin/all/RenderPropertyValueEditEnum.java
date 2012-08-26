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

package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.builtin.RenderPropertyValueEdit;
import woko.facets.builtin.WokoFacets;
import woko.util.Util;

/**
 * Catch-all edit property facet. Handles enums, and defaults to renderPropertyValue (read-only) otherwise.
 */
@FacetKey(name = WokoFacets.renderPropertyValueEdit, profileId = "all", targetObjectType = Object.class)
public class RenderPropertyValueEditEnum extends RenderPropertyValueImpl implements RenderPropertyValueEdit {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/all/renderPropertyValueEditEnum.jsp";

    private Class<?> enumClass;

    @Override
    public String getPath() {
        Class<?> propType = null;
        Object propVal = getPropertyValue();
        if (propVal!=null) { // look on property instance first
            propType = propVal.getClass();
        }
        if (propType==null) {
            // no instance (prop value is null), use the compile-time type
            propType = Util.getPropertyType(getOwningObject().getClass(), getPropertyName());
        }
        // check if the property type is an enum
        if (propType.isEnum()) {
            enumClass = propType;
            return FRAGMENT_PATH;
        }

        // not an enum, fallback to the render prop value
        getRequest().setAttribute(WokoFacets.renderPropertyValue, this); // need to bind this as a renderPropertyValue for read-only views to work
        return super.getPath();
    }

    public Class<?> getEnumClass() {
        return enumClass;
    }
}

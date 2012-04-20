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
import woko.facets.builtin.RenderPropertyValue;
import woko.facets.builtin.RenderPropertyValueEdit;
import woko.facets.builtin.WokoFacets;
import woko.persistence.ObjectStore;
import woko.persistence.ResultIterator;
import woko.util.Util;

import java.util.List;

@FacetKey(name = WokoFacets.renderPropertyValueEdit, profileId = "all")
public class RenderPropertyValueEditXToOneRelation extends RenderPropertyValueImpl implements RenderPropertyValueEdit {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/all/renderPropertyValueEditXToOneRelation.jsp";

    public String getPath() {
        // we check if the target object is persistent or not.
        // if persistent, then forward to another JSP
        ObjectStore os = getFacetContext().getWoko().getObjectStore();
        List<Class<?>> mappedClasses = os.getMappedClasses();
        Class<?> propertyType = getPropertyType();
        if (mappedClasses.contains(propertyType)) {
            return FRAGMENT_PATH;
        }
        getRequest().setAttribute(RenderPropertyValue.FACET_NAME, this); // NEEDED because the JSP expects the facet to be found.
        return super.getPath();
    }

    private Class<?> getPropertyType() {
        return Util.getPropertyType(getOwningObject().getClass(), getPropertyName());
    }

    public List<?> getChoices() {
        ObjectStore store = getFacetContext().getWoko().getObjectStore();
        ResultIterator<?> choices = store.list(store.getClassMapping(getPropertyType()), 0, Integer.MAX_VALUE);
        return choices.toList();
    }

}

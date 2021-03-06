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

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.builtin.RenderPropertyValue;
import woko.facets.builtin.RenderPropertyValueEdit;
import woko.facets.builtin.WokoFacets;
import woko.persistence.ObjectStore;
import woko.persistence.ResultIterator;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import java.util.List;

/**
 * Generic <code>renderPropertyValueEdit</code> facet for x-to-one properties (relationships
 * between Woko-managed POJOs). Should allow to change the end of the relation by selecting from
 * a list of candidates.
 */
@FacetKey(name = WokoFacets.renderPropertyValueEdit, profileId = "all")
public class RenderPropertyValueEditXToOneRelation<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseRenderPropertyValueEdit<OsType,UmType,UnsType,FdmType> implements RenderPropertyValueEdit {

    public static final String ENUM_FRAGMENT_PATH = "/WEB-INF/woko/jsp/all/renderPropertyValueEditEnum.jsp";
    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/all/renderPropertyValueEditXToOneRelation.jsp";

    /**
     * Return the appropriate JSP, depending on the type of the relation end.
     * Basically checks if the target type is a Woko-managed class, and defaults to
     * <code>renderPropertyValue</code> if the relation cannot be modified.
     * Also handles enumerated types (Java enums).
     *
     * @return the path to the JSP fragment to be used
     */
    public String getPath() {
        // check if the property is an enum
        Class<?> propertyType = getPropertyType();
        if (propertyType!=null) {
            if (propertyType.isEnum()) {
                return ENUM_FRAGMENT_PATH;
            }

            // not an enum : we check if the target object is persistent or not.
            // if persistent, then forward to another JSP
            OsType os = getFacetContext().getWoko().getObjectStore();
            List<Class<?>> mappedClasses = os.getMappedClasses();
            if (mappedClasses.contains(propertyType)) {
                return FRAGMENT_PATH;
            }
        }
        getRequest().setAttribute(RenderPropertyValue.FACET_NAME, this); // NEEDED because the JSP expects the facet to be found.
        return super.getPath();
    }

    /**
     * Return a list of elements to choose from for assigning the relation end.
     * @return a list of choices for the relation end
     */
    public List<?> getChoices() {
        OsType store = getFacetContext().getWoko().getObjectStore();
        ResultIterator<?> choices = store.list(store.getClassMapping(getPropertyType()), 0, Integer.MAX_VALUE);
        return choices.toList();
    }

}

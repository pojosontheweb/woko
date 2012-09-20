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
import woko.util.Util;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

@FacetKey(name = WokoFacets.renderPropertyValueEdit, profileId = "all", targetObjectType = Collection.class)
public class RenderPropertyValueEditXToManyRelation<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends RenderPropertyValueImpl<OsType,UmType,UnsType,FdmType> implements RenderPropertyValueEdit {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/all/renderPropertyValueEditXToManyRelation.jsp";

    public String getPath() {
        // we check if the target object is persistent or not.
        // if persistent, then forward to another JSP
        Class<?> compoundType = getCompoundType();
        ObjectStore os = getFacetContext().getWoko().getObjectStore();
        List<Class<?>> mappedClasses = os.getMappedClasses();
        if (mappedClasses.contains(compoundType)) {
            return FRAGMENT_PATH;
        }

        // fallback to view if can't use the collection compound type
        getRequest().setAttribute(RenderPropertyValue.FACET_NAME, this); // NEEDED because the JSP expects the facet to be found.
        return super.getPath();
    }

    private Class<?> getCompoundType() {
        Type[] genTypes = Util.getPropertyGenericTypes(getOwningObject().getClass(), getPropertyName());
        if (genTypes!=null && genTypes.length>0) {
            Type t = genTypes[0];
            if (t instanceof Class<?>) {
                return (Class<?>)t;
            }
        }
        return null;
    }

    public List<?> getChoices() {
        OsType store = getFacetContext().getWoko().getObjectStore();
        Class<?> compoundType = getCompoundType();
        ResultIterator<?> choices = store.list(store.getClassMapping(compoundType), 0, Integer.MAX_VALUE);
        return choices.toList();
    }

}

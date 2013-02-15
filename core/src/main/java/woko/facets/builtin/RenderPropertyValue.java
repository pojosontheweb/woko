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

import woko.facets.FragmentFacet;
import net.sourceforge.jfacets.IFacet;

/**
 * <code>renderPropertyValue</code> fragment facets are used to display the value of an object's
 * property. There are two ways to use this facet :
 * <ul>
 *     <li>
 *         Type-based : the facet is looked up using the property type. If the property is a
 *         <code>String</code>, then the Object Renderer will look for a
 *         <code>(renderPropertyValue,myRole,String)</code> facet.
 *     </li>
 *     <li>
 *         Name-based : the facet is looked up using the property name. If the property name is
 *         "foo" and the owning object is "MyClass", then the Object Renderer will look for a
 *         <code>renderPropertyValue_foo,myRole,MyClass</code> facet.
 *     </li>
 * </ul>
 *
 * Note that the target type is different in the two modes. You must choose the target type depending on
 * the mode you use (type-based : target type is the property type, name-based : target type is the
 * owning object's type).
 *
 * Of course, inheritance and roles still work as usual, so you can use type or name-based
 * <code>renderPropertyValue</code> using supertypes etc.
 */
public interface RenderPropertyValue extends IFacet, FragmentFacet {

    static final String FACET_NAME = "renderPropertyValue";

    /**
     * Set the owning object (owner of the property)
     * @param o the owning object
     */
    void setOwningObject(Object o);

    /**
     * Return the owning object (owner of the property)
     * @return the owning object
     */
    Object getOwningObject();

    /**
     * Set the name of the property
     * @param name the property name
     */
    void setPropertyName(String name);

    /**
     * Return the name of the property
     * @return the property name
     */
    String getPropertyName();

    /**
     * Return the value of the property
     * @return the value of the property
     */
    Object getPropertyValue();

    /**
     * Set the value of the property
     * @param v the value of the property
     */
    void setPropertyValue(Object v);

    /**
     * Return the type of the property. Run-time type should be used of available (non null prop).
     * @return the type of the property
     */
    Class<?> getPropertyType();

}
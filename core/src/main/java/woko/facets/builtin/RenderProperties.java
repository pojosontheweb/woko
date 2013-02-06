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

import java.util.List;
import java.util.Map;

/**
 * <code>renderProperties</code> fragment facets are used to display the properties
 * of target objects in the page.
 *
 * By default, it uses reflection in order to get the first level properties of the target object
 * and then delegates to other Object Renderer facets (<code>renderPropertyName</code> and
 * <code>renderPropertyValue</code>).
 *
 * One can override this facet in order to customize the properties section of rendered objects
 * for the various classes and roles of the app.
 */
public interface RenderProperties extends IFacet, FragmentFacet {

    static final String FACET_NAME = "renderProperties";

    /**
     * Return a list of the property names to be displayed
     * @return a list of property names to be displayed
     */
    List<String> getPropertyNames();

    /**
     * Return a map of the property names and values to be displayed
     * @return a map of property names and values to be displayed
     */
    Map<String, Object> getPropertyValues();

}
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
 * <code>renderObject</code> fragment facets are used to render the target object
 * in a page, including the title, links and properties.
 *
 * This facet can be overriden in order to replace the whole rendering of an object
 * for your role(s) and class(es). For more fine-grained customization, one can also
 * override the other Object Renderer facets (<code>renderTitle</code>, <code>renderProperties</code>
 * etc.).
 */
public interface RenderObject extends IFacet, FragmentFacet {

    static final String FACET_NAME = "renderObject";

}
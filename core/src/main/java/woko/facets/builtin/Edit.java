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

import woko.facets.ResolutionFacet;

/**
 * <code>edit</code> facet : used to edit managed POJOs with urls like :
 * <p/>
 * <pre>http://.../edit/MyClass/123</pre>
 * <p/>
 * This facet is the entry point of the Object Renderer in edit mode. By default, it delegates
 * to the other ObjectRenderer facets (<code>renderPropertiesEdit</code>,
 * <code>renderProperyValueEdit</code> etc.), in order to create a FORM for the target object.
 * <p/>
 * You can completely replace the edit JSP for your target objects / roles by overriding this facet.
 * For finer-grained control over the ObjectRenderer, simply assign this facet to your class / role and
 * override sub-parts of the renderer.
 */
public interface Edit extends ResolutionFacet {

    /**
     * Facet name constant
     */
    static final String FACET_NAME = "edit";

    /**
     * Return the JSP path to be used. By default, returns the ObjectRenderer JSP for editing
     * objects. Override to replace the "edit" page for your Class(es)/Role(s).
     * @return the JSP path
     */
    String getFragmentPath();

}
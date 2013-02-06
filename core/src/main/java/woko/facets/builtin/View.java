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
 * <code>view</code> resolution facets are used to display Woko-managed POJOs as HTML pages.
 *
 * One can completely replace the Object Renderer for objects displayed in view mode by overriding this facet.
 * For more fine-grained control, override some Object Renderer fragment facets (<code>renderObject</code>,
 * <code>renderProperties</code>, etc.).
 */
public interface View extends ResolutionFacet {

    static final String FACET_NAME = "view";

}

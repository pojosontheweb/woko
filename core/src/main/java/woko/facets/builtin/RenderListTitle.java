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

import net.sourceforge.jfacets.IFacet;
import woko.facets.FragmentFacet;

/**
 * <code>renderListTitle</code> fragment facets are used to display a title in the list of objects.
 *
 * Can be ovverriden to customize the titles for objects and roles of your app.
 */
public interface RenderListTitle extends IFacet, FragmentFacet {

    static final String FACET_NAME = "renderListTitle";

    /**
     * Return a human-readable title used in the list of the target object
     * @return the list title for the target object
     */
    String getTitle();

}
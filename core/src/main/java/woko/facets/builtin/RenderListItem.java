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
 * <code>renderListItem</code> facets are used by <code>list</code> in order to display
 * each object fragment.
 *
 * By default it'll render the object's title, along with an hyperlink if the <code>view</code>
 * facet is available for the target object and the current user's role.
 *
 * One can override this facet in order to customize the display of listed objects for various
 * roles and classes in the app.
 */
public interface RenderListItem extends IFacet, FragmentFacet {

    static final String FACET_NAME = "renderListItem";

    /**
     * Return the optional CSS class to be used for  the list item container
     * @return optional CSS class for the list item container
     */
    public String getItemWrapperCssClass();

}
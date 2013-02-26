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
import woko.facets.builtin.all.Link;

import java.util.List;

/**
 * <code>navBar</code> facet, used to display the navigation block.
 *
 * This facet can be overriden for your role(s) in order to change the nav bar for the various
 * users of the app. The target type can be used as well, but won't be available everywhere (basically only
 * for built-in facets that use a target object, like <code>view</code> or <code>edit</code>, but not
 * for facets like <code>home</code> that are not assigned to a particular target type).
 */
public interface NavBar extends FragmentFacet {

    static final String FACET_NAME = "navBar";

    List<Link> getLinks();

}
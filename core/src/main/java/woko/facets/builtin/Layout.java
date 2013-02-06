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

import java.util.List;

/**
 * <code>layout</code> facets are responsible for the global templating of your application.
 * <p/>
 * They provide access to :
 * <ul>
 * <li>The app title (used in page titles etc.)</li>
 * <li>The path to the Stripes layout JSP to be used (page templating)</li>
 * <li>An optional list of CSS includes</li>
 * <li>An optional list of JS includes</li>
 * </ul>
 *
 * This facet can be overriden for your role(s) in order to change the layout for the various
 * users of the app. The target type can be used as well, but won't be available everywhere (basically only
 * for built-in facets that use a target object, like <code>view</code> or <code>edit</code>, but not
 * for facets like <code>home</code> that are not assigned to a particular target type).
 */
public interface Layout extends IFacet {

    static final String FACET_NAME = "layout";

    /**
     * Return the app title, for use in page titles etc.
     * @return the app title
     */
    String getAppTitle();

    /**
     * Optional CSS includes (included in <code><head/></code> section)
     * @return an optional list of URLs to CSS resources to be included in the page
     */
    List<String> getCssIncludes();

    /**
     * Optional JavaScript includes (included in <code><head/></code> section)
     * @return an optional list of URLs to JS resources to be included in the page
     */
    List<String> getJsIncludes();

    /**
     * Return the path to the Stripes layout to be used as a page template
     * @return the path to the Stripes layout
     */
    String getLayoutPath();

}

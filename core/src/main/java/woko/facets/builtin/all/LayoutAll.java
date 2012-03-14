/*
 * Copyright 2001-2010 Remi Vankeisbelck
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

package woko.facets.builtin.all;


import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.BaseFacet;
import woko.facets.builtin.Layout;
import woko.facets.builtin.WokoFacets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@FacetKey(name= WokoFacets.layout, profileId="all")
public class LayoutAll extends BaseFacet implements Layout {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/all/layout.jsp";

    public String getAppTitle() {
    return "Woko";
  }

  public List<String> getCssIncludes() {
    return Collections.emptyList();
  }

  public List<String> getJsIncludes() {
    return Collections.emptyList();
  }

  public String getLayoutPath() {
    return FRAGMENT_PATH;
  }


}

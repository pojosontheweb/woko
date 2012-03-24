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

package woko.facets.builtin.hbcompass;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.BaseFragmentFacet;
import woko.facets.WokoFacetContext;
import woko.hbcompass.HibernateCompassStore;
import woko.hbcompass.tagcloud.CompassCloud;
import woko.hbcompass.tagcloud.CompassCloudElem;

import java.util.Collection;

@FacetKey(name = "termCloudFragment", profileId = "developer", targetObjectType = CompassCloud.class)
public class TermCloudFragmentFacet extends BaseFragmentFacet {

  public String getPath() {
    return "/WEB-INF/woko/jsp/hbcompass/term-cloud-fragment.jsp";
  }

  public Collection<CompassCloudElem> getCloudElems() {
    WokoFacetContext facetContext = getFacetContext();
    HibernateCompassStore hbc = (HibernateCompassStore) facetContext.getWoko().getObjectStore();
    return ((CompassCloud) facetContext.getTargetObject()).getCloud(hbc.getCompass());
  }


}

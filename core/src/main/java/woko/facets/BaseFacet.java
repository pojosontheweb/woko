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

package woko.facets;

import net.sourceforge.jfacets.IFacet;
import net.sourceforge.jfacets.IFacetContext;
import net.sourceforge.jfacets.IInstanceFacet;
import woko.Woko;
import woko.facets.WokoFacetContext;
import woko.persistence.ObjectStore;

import javax.servlet.http.HttpServletRequest;

public abstract class BaseFacet implements IFacet {

  private WokoFacetContext facetContext;

  public WokoFacetContext getFacetContext() {
    return facetContext;
  }

  public void setFacetContext(IFacetContext iFacetContext) {
    this.facetContext = (WokoFacetContext)iFacetContext;
  }

    public Woko getWoko() {
        return getFacetContext().getWoko();
    }

    public ObjectStore getObjectStore() {
        return getWoko().getObjectStore();
    }

    public HttpServletRequest getRequest() {
        return facetContext.getRequest();
    }

}

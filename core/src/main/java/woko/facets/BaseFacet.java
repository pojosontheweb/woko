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

package woko.facets;

import net.sourceforge.jfacets.IFacet;
import net.sourceforge.jfacets.IFacetContext;
import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.IInstanceFacet;
import woko.Woko;
import woko.facets.WokoFacetContext;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import javax.servlet.http.HttpServletRequest;

/**
 * Base facet class.
 */
public abstract class BaseFacet<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > implements IFacet {

    /**
     * Ref to the facet context
     */
    private WokoFacetContext<OsType, UmType, UnsType, FdmType> facetContext;

    /**
     * Return the <code>WokoFacetContext</code> for this facet
     * @return the <code>WokoFacetContext</code>
     */
    public WokoFacetContext<OsType, UmType, UnsType, FdmType> getFacetContext() {
        return facetContext;
    }

    /**
     * Set the <code>WokoFacetContext</code> for this facet
     * @param iFacetContext the <code>WokoFacetContext</code>
     */
    @SuppressWarnings("unchecked")
    public void setFacetContext(IFacetContext iFacetContext) {
        this.facetContext = (WokoFacetContext<OsType, UmType, UnsType, FdmType>) iFacetContext;
    }

    /**
     * Return the <code>Woko</code> instance
     * @return the <code>Woko</code> instance
     */
    public Woko<OsType, UmType, UnsType, FdmType> getWoko() {
        return getFacetContext().getWoko();
    }

    /**
     * Return the <code>ObjectStore</code>
     * @return the <code>ObjectStore</code>
     */
    public OsType getObjectStore() {
        return getWoko().getObjectStore();
    }

    /**
     * Return the <code>request</code>
     * @return the <code>request</code>
     */
    public HttpServletRequest getRequest() {
        return facetContext.getRequest();
    }

}

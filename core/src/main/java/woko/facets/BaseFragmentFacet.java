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

import net.sourceforge.jfacets.IFacetDescriptorManager;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import javax.servlet.http.HttpServletRequest;

/**
 * Base class for <code>FragmentFacet</code>s.
 */
public abstract class BaseFragmentFacet<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseFacet<OsType, UmType, UnsType, FdmType> implements FragmentFacet {

    /**
     * Return the path to the JSP fragment (as returned by <code>getPath()</code>)
     * @param request the request
     * @return the path to the JSP fragment
     */
    public String getFragmentPath(HttpServletRequest request) {
        return this.getPath();
    }

    /**
     * Return the path to the JSP fragment
     * @return the path to the JSP fragment
     */
    public abstract String getPath();

}

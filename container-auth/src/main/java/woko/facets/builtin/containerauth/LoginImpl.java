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

package woko.facets.builtin.containerauth;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.*;
import woko.facets.BaseResolutionFacet;
import woko.facets.builtin.Login;
import woko.facets.builtin.WokoFacets;

/**
 * <code>login</code> facet that simply redirects to <code>/home</code>. When using container authentication
 * the <code>/login</code> URL is supposed to be protected (in web.xml) so that container will
 * prompt for authentication before the user can reach this facet.
 */
@FacetKey(name= WokoFacets.login, profileId="all")
@StrictBinding
public class LoginImpl extends BaseResolutionFacet implements Login {

  public Resolution getResolution(ActionBeanContext abc) {
    abc.getMessages().add(new LocalizableMessage("woko.login.success"));
    return new RedirectResolution("/home");
  }


}

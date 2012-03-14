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

package woko.inmemory

import woko.Woko
import net.sourceforge.stripes.mock.MockServletContext
import net.sourceforge.stripes.controller.StripesFilter
import net.sourceforge.stripes.controller.DispatcherServlet
import woko.actions.WokoActionBean
import net.sourceforge.stripes.mock.MockRoundtrip
import javax.servlet.ServletException
import woko.facets.FacetNotFoundException
import woko.persistence.ObjectStore
import woko.users.UserManager
import woko.WokoInitListener

abstract class InMemRoundtripTestBase extends GroovyTestCase {

  Woko createWoko(String username) {
    InMemoryObjectStore store = new InMemoryObjectStore()
    store.addObject('1', new Book([_id: '1', name: 'Moby Dick', nbPages: 123]))
    UserManager userManager = new InMemoryUserManager()
    userManager.addUser("wdevel", "wdevel", ["developer"])
    Woko inMem = new Woko(
        store,
        userManager,
        [Woko.ROLE_GUEST],
        Woko.createFacetDescriptorManager(Woko.DEFAULT_FACET_PACKAGES),
        new DummyURS(username:username));

    return inMem
  }

  MockServletContext createMockServletContext(String username) {
    Map<String, String> params = new HashMap<String, String>();
    params.put("ActionResolver.Packages", "woko.actions");
    params.put("Extension.Packages", "woko.actions");
    MockServletContext mockServletContext = new MockServletContext("basicroundtrip");
    mockServletContext.addFilter(StripesFilter.class, "StripesFilter", params);
    mockServletContext.setServlet(DispatcherServlet.class, "DispatcherServlet", null);
    Woko woko = createWoko(username)
    mockServletContext.setAttribute Woko.CTX_KEY, woko
    return mockServletContext;
  }

  WokoActionBean trip(String username, String facetName, String className, String key) {
    return trip(username, facetName, className, key, null)
  }

  WokoActionBean trip(String username, String facetName, String className, String key, Map params) {
    def c = createMockServletContext(username)
    StringBuilder url = new StringBuilder('/').append(facetName)
    if (className) {
      url << '/'
      url << className
    }
    if (key) {
      url << '/'
      url << key
    }
    MockRoundtrip t = new MockRoundtrip(c, url.toString())
    if (params) {
      params.each { k, v ->
        t.addParameter(k, v)
      }
    }
    t.execute()
    WokoActionBean ab = t.getActionBean(WokoActionBean.class)
    assert ab
    return ab
  }

  void assertFacetNotFound(String username, String facetName, String className, String key) {
    boolean hasThrown = false
    try {
      trip(username, facetName, className, key)
    } catch (Exception e) {
      if (e instanceof ServletException) {
        hasThrown = e.cause instanceof FacetNotFoundException
      }
    }
    assert hasThrown
  }


}

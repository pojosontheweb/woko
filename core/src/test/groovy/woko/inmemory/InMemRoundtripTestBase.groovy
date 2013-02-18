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

package woko.inmemory

import woko.ioc.SimpleWokoIocContainer
import woko.mock.MockUsernameResolutionStrategy
import woko.mock.MockUtil

import javax.servlet.ServletException
import net.sourceforge.stripes.controller.DispatcherServlet
import net.sourceforge.stripes.controller.StripesFilter
import net.sourceforge.stripes.mock.MockRoundtrip
import net.sourceforge.stripes.mock.MockServletContext
import testentity.MyValidatedPojo
import woko.Woko
import woko.actions.WokoActionBean
import woko.facets.FacetNotFoundException
import woko.users.UserManager

abstract class InMemRoundtripTestBase extends GroovyTestCase {

    Woko createWoko(String username) {
        InMemoryObjectStore store = new InMemoryObjectStore()
        store.addObject('1', new Book([_id: '1', name: 'Moby Dick', nbPages: 123]))
        store.addObject('2', new MyValidatedPojo(id: 1, str: "cannotbenull"))
        UserManager userManager = new InMemoryUserManager()
        userManager.addUser("wdevel", "wdevel", ["developer"])
        SimpleWokoIocContainer ioc = new SimpleWokoIocContainer(
                store,
                userManager,
                new MockUsernameResolutionStrategy(username),
                Woko.createFacetDescriptorManager(Woko.DEFAULT_FACET_PACKAGES))
        return new Woko(ioc, [Woko.ROLE_GUEST])
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

    def doWithMockContext(String username, Closure c) {
        new MockUtil().withServletContext(createWoko(username), { MockServletContext ctx->
            c(ctx)
        } as MockUtil.Callback)
    }

//    MockRoundtrip mockRoundtrip(MockServletContext ctx, String url, Map params) {
//        MockRoundtrip t = new MockRoundtrip(ctx, url.toString())
//        if (params) {
//            params.each { String k, String v ->
//                t.addParameter(k, v)
//            }
//        }
//        t.execute()
//        return t
//    }
//
//    MockRoundtrip mockRoundtrip(MockServletContext ctx, String facetName, String className, String key, Map params) {
//        StringBuilder url = new StringBuilder('/').append(facetName)
//        if (className) {
//            url << '/'
//            url << className
//        }
//        if (key) {
//            url << '/'
//            url << key
//        }
//        mockRoundtrip(ctx, url.toString(), params)
//    }

    WokoActionBean trip(String username, String facetName, String className, String key) {
        trip(username, facetName, className, key, null)
    }

    WokoActionBean trip(String username, String facetName, String className, String key, Map params) {
        def res
        doWithMockContext(username) { MockServletContext ctx ->
            MockRoundtrip trip = MockUtil.mockRoundtrip(ctx, facetName, className, key, params)
            res = trip.getActionBean(WokoActionBean.class)
        }
        return res
    }

    void assertFacetNotFound(String username, String facetName, String className, String key) {
        boolean hasThrown = false
        try {
            trip(username, facetName, className, key)
        } catch (Exception e) {
            if (e instanceof ServletException) {
                hasThrown = e.cause instanceof FacetNotFoundException
            } else {
                throw e
            }
        }
        assert hasThrown
    }


}
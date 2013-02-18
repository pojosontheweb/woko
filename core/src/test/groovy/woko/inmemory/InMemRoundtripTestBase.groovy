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

import net.sourceforge.stripes.action.ActionBean
import net.sourceforge.stripes.mock.MockHttpSession
import woko.ioc.SimpleWokoIocContainer

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

import javax.servlet.http.HttpServlet

abstract class InMemRoundtripTestBase extends GroovyTestCase {

    private CloseableMockRoundtrip currentCloseableTrip = null

    Woko createWoko(String username) {
        InMemoryObjectStore store = new InMemoryObjectStore()
        store.addObject('1', new Book([_id: '1', name: 'Moby Dick', nbPages: 123]))
        store.addObject('2', new MyValidatedPojo(id: 1, str: "cannotbenull"))
        UserManager userManager = new InMemoryUserManager()
        userManager.addUser("wdevel", "wdevel", ["developer"])

        SimpleWokoIocContainer ioc = new SimpleWokoIocContainer(store, userManager, new DummyURS(username: username), Woko.createFacetDescriptorManager(Woko.DEFAULT_FACET_PACKAGES))

        Woko inMem = new Woko(ioc, [Woko.ROLE_GUEST])

        return inMem
    }

    @Override
    protected void tearDown() throws Exception {
        if (currentCloseableTrip) {
            currentCloseableTrip.close()
        }
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

    CloseableMockRoundtrip createRoundtrip(String username, String url) {
        def c = createMockServletContext(username)
        currentCloseableTrip = new CloseableMockRoundtrip(c, url.toString())
        return currentCloseableTrip
    }

    MockRoundtrip mockRoundtrip(String username, String facetName, String className, String key, Map params) {
        StringBuilder url = new StringBuilder('/').append(facetName)
        if (className) {
            url << '/'
            url << className
        }
        if (key) {
            url << '/'
            url << key
        }
        MockRoundtrip t = createRoundtrip(username, url.toString())
        if (params) {
            params.each { k, v ->
                t.addParameter(k, v)
            }
        }
        t.execute()
        return t
    }

    WokoActionBean trip(String username, String facetName, String className, String key) {
        return trip(username, facetName, className, key, null)
    }

    WokoActionBean trip(String username, String facetName, String className, String key, Map params) {
        mockRoundtrip(username, facetName, className, key, params).getActionBean(WokoActionBean.class)
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

// Workaround http://www.stripesframework.org/jira/browse/STS-725
// TODO remove when fixed in Stripes
class CloseableMockRoundtrip extends MockRoundtrip {

    CloseableMockRoundtrip(MockServletContext context, Class<? extends ActionBean> beanType) {
        super(context, beanType)
    }

    CloseableMockRoundtrip(MockServletContext context, Class<? extends ActionBean> beanType, MockHttpSession session) {
        super(context, beanType, session)
    }

    CloseableMockRoundtrip(MockServletContext context, String actionBeanUrl) {
        super(context, actionBeanUrl)
    }

    CloseableMockRoundtrip(MockServletContext context, String actionBeanUrl, MockHttpSession session) {
        super(context, actionBeanUrl, session)
    }

    void close() {
        println "Destroying Filters & Servlets"
        this.context.filters.each { it.destroy() }
        this.context.servlets.each { HttpServlet s -> s.destroy() }
    }
}
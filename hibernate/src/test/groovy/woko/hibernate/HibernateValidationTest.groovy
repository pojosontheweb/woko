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

package woko.hibernate

import woko.Woko
import net.sourceforge.stripes.controller.DispatcherServlet
import net.sourceforge.stripes.controller.StripesFilter
import net.sourceforge.stripes.mock.MockServletContext
import woko.inmemory.InMemoryUserManager
import woko.users.UserManager
import woko.actions.WokoActionBean
import net.sourceforge.stripes.mock.MockRoundtrip

import junit.framework.TestCase
import org.hibernate.Transaction
import org.hibernate.Session
import net.sourceforge.stripes.validation.ValidationErrors
import net.sourceforge.stripes.validation.ValidationError

class HibernateValidationTest extends TestCase {

    Woko createWoko(String username) {
        def store = new HibernateStore(["entities"])
        //store.addObject('1', new Book([_id: '1', name: 'Moby Dick', nbPages: 123]))
        UserManager userManager = new InMemoryUserManager()
        userManager.addUser("wdevel", "wdevel", ["developer"])
        Woko inMem = new Woko(
                store,
                userManager,
                [Woko.ROLE_GUEST],
                Woko.createFacetDescriptorManager(Woko.DEFAULT_FACET_PACKAGES),
                new DummyURS(username: username));

        return inMem
    }

    MockServletContext createMockServletContext(String username) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("ActionResolver.Packages", "woko.actions");
        params.put("Extension.Packages", "woko.actions,woko.hibernate");
        MockServletContext mockServletContext = new MockServletContext("basicroundtrip");
        mockServletContext.addFilter(StripesFilter.class, "StripesFilter", params);
        mockServletContext.setServlet(DispatcherServlet.class, "DispatcherServlet", null);
        Woko woko = createWoko(username)
        mockServletContext.setAttribute woko.Woko.CTX_KEY, woko
        return mockServletContext;
    }

    MockRoundtrip trip(String username, String facetName, String className, String key) {
      return trip(username, facetName, className, key, null)
    }

    MockRoundtrip trip(String username, String facetName, String className, String key, Map params) {
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
      return t
    }

    private void doInTx(store,Closure c) {
      Session s = store.getSessionFactory().getCurrentSession()
      Transaction tx = s.beginTransaction()
      println "Started tx $tx"
      try {
        c.call(store)
        println "Commiting  $tx"
        tx.commit()
      } catch(Throwable e) {
        println "Exception caught : $e, Roll-backing and rethrowing"
        tx.rollback()
        throw new RuntimeException("Exception within tx", e)
      }
    }



    void testEntityValidation() {
        WokoActionBean ab = trip("wdevel", "save", "MyEntity", null, [
                "object.id":"1",
                "object.name":"foobar",
                "object.otherProp":"baz"
        ]).getActionBean(WokoActionBean.class)
        assertNotNull("unable to get WokoActionBean", ab)
        assertEquals("invalid property value", ab.object.name, "foobar")

        // now attempt to save without required value
        ab = trip("wdevel", "save", "MyEntity", null, [
                "object.id":"2",
                "object.otherProp":"baz"
        ]).getActionBean(WokoActionBean.class)

        assertNotNull("unable to get WokoActionBean", ab)
        assertNull("name should be null", ab.object.name)
        // make sure we have a validation error
        ValidationErrors errors = ab.context.validationErrors
        assertEquals("invalid validation error count", 1, errors.size())
        String expectedKey = "object.name"
        assertTrue("key $expectedKey not found", errors.containsKey(expectedKey))
        List<ValidationError> lve = errors[expectedKey]
        assertEquals("invalid validation error count for key $expectedKey", 1, lve.size())
        ValidationError e = lve[0]
        assertEquals("unexpected field name in validation error", expectedKey, e.fieldName)
//        String msg = e.getMessage(ab.context.locale) // TODO assert but there's a bug in Stripes, NPE thrown (no StripesFilter.getConfiguration()
//        println msg
    }

}

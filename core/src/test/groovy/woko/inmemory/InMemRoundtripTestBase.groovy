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

import net.sourceforge.stripes.mock.MockHttpSession
import net.sourceforge.stripes.mock.MockServletContext
import testentity.MyValidatedPojo
import woko.Woko
import woko.ioc.SimpleWokoIocContainer
import woko.mock.MockUsernameResolutionStrategy
import woko.mock.MockUtil
import woko.users.UserManager

abstract class InMemRoundtripTestBase extends GroovyTestCase {

    static Woko createWoko(String username) {
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

    static def doWithMockContext(String username, Closure c) {
        new MockUtil().withServletContext(createWoko(username), { MockServletContext ctx ->
            c(ctx)
        } as MockUtil.Callback)
    }

    static void assertFacetNotFound(String username, String facetName, String className, String key) {
        doWithMockContext(username) { MockServletContext c ->
            assert MockUtil.throwsFacetNotFound(c, facetName, className, key, [:], null)
        }
    }

}
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

package woko.webtests.containerauth

class BindingSecurityTest extends WebTestBase {

    void testBindingSecurityOnFacet() {
        webtest("testBindingSecurityOnFacet") {

            login()
            goToPage '/save/MyEntity?object.id=1&object.prop1=val1&object.prop2=shouldnotbind'
            try {

                goToPage '/bindMe/MyEntity/1'
                verifyText 'Binding security tests'

                goToPage '/bindMe/MyEntity/1?facet.alwaysBound=funk2live'
                verifyText 'alwaysBound=funk2live'
                verifyText 'neverBound=shouldnotchange'
                verifyText 'other.foo=bar'

                goToPage '/bindMe/MyEntity/1?facet.alwaysBound=funk2live&facet.neverBound=punkisnotdead'
                verifyText 'alwaysBound=funk2live'
                verifyText 'neverBound=shouldnotchange'
                not {
                    verifyText 'neverBound=punkisnotdead'
                }
                verifyText 'other.foo=bar'

                goToPage '/bindMe/MyEntity/1?other.foo=bazzz'
                verifyText 'other.foo=bar'
                not {
                    verifyText 'other.foo=bazzz'
                }
            } finally {
                goToPage '/delete/MyEntity/1?facet.confirm=true'
            }

        }
    }

    void testBindingSecurityOnObject() {
        webtest("testBindingSecurityOnObject") {

            login()
            goToPage '/save/MyEntity?object.id=1&object.prop1=val1&object.prop2=123456'
            try {

                verifyText 'Object saved'

                goToPage '/view/MyEntity/1'
                not {
                    verifyText '123456'
                }

            } finally {
                goToPage '/delete/MyEntity/1?facet.confirm=true'
            }
        }
    }


}

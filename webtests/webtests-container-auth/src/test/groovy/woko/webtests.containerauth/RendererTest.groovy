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

class RendererTest extends WebTestBase {

    void testPropertyValueFacetOverrides() {
        webtest('test prop value overrides') {
            login()
            // create test object
            goToPage '/save/MyBook?createTransient=true&object._id=1111&object.name=Moby&object.nbPages=123'

            // view
            goToPage '/view/MyBook/1111'
            verifyText '123 page(s)' // view override

            // edit
            goToPage '/edit/MyBook/1111'
            verifyText 'page(s)'

            goToPage '/delete/MyBook/1111?facet.confirm=true'
        }
    }

    void testFlatLayout() {
        webtest("test flat layout") {
            login()
            try {
                goToPage '/save/MyEntity?createTransient=true&object.id=1&object.prop1=abc&object.prop2=123'

                goToPage '/view/MyEntity/1'
                verifyXPath xpath: '/html/body/div/div[3]/div/div/div/div[3]/span/span', text: '.*abc.*', regex: true
            } finally {
                goToPage '/delete/MyEntity/1?facet.confirm=true'
            }
        }
    }

    void testComboForPersistentXToOne() {
        webtest("testComboForPersistentXToOne") {
            login()
            try {
                goToPage '/save/EntityWithRelations?createTransient=true&object.id=111&object.name=ewr1'
                goToPage '/save/SubEntity?createTransient=true&object.id=111&object.name=sub1'

                goToPage '/edit/SubEntity/111'
                verifySelectField name:'object.daEntity', value:''

                setSelectField name:'object.daEntity', value:'111'
                clickButton name:'save'
                verifySelectField name:'object.daEntity', value:'111'
                verifySelectField name:'object.daEntity', text:'ewr1'

            } finally {
                goToPage '/delete/SubEntity/111?facet.confirm=true'
                goToPage '/delete/EntityWithRelations/111?facet.confirm=true'
            }
        }
    }

}

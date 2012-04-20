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

package woko.webtests.containerauth

class RendererTest extends WebTestBase {

    void testPropertyValueFacetOverrides() {
        webtest('test prop value overrides') {
            login()
            // create test object
            goToPage '/save/MyBook?object._id=1111&object.name=Moby&object.nbPages=123'

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
            goToPage '/save/MyEntity?object.id=1&object.prop1=abc&object.prop2=123'

            goToPage '/view/MyEntity/1'
            verifyXPath xpath: '/html/body/div/div[3]/div/div/div/div[3]/span/span', text: '.*abc.*', regex: true
        }
    }

    void testComboForPersistentXToOne() {
        webtest("testComboForPersistentXToOne") {
            login()
            try {
                goToPage '/save/EntityWithRelations?object.id=1&object.name=ewr1'
                goToPage '/save/SubEntity?object.id=1&object.name=sub1'

                goToPage '/edit/SubEntity/1'
                verifyText 'bar'
                verifySelectField name:'object.daEntity', value:''

                setSelectField name:'object.daEntity', optionIndex:1
                clickButton name:'save'
                verifySelectField name:'object.daEntity', value:'1'
                verifySelectField name:'object.daEntity', text:'ewr1'

            } finally {
                goToPage '/delete/EntityWithRelations/1?facet.confirm=true'
                goToPage '/delete/SubEntity/1?facet.confirm=true'
            }
        }
    }

}

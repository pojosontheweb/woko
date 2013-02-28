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

package woko.webtests.bootstrap

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
            goToPage '/save/MyEntity?createTransient=true&object.id=5566&object.prop1=abc&object.prop2=123'

            goToPage '/view/MyEntity/5566'
            verifyXPath xpath: '/html/body/div/div[2]/div/div[3]/div[2]/div[3]/div[2]/span/span', text: '.*abc.*', regex: true

            goToPage '/delete/MyEntity/5566?facet.confirm=true'
        }
    }

    void testComboForPersistentXToOne() {
        webtest("testComboForPersistentXToOne") {
            login()
            try {
                goToPage '/save/EntityWithRelations?createTransient=true&object.name=ewr1'
                storeXPath xpath:'/html/body/div/div[2]/div/div[3]/div[2]/form/fieldset/div[2]/div/input/@value', property:'id1'
                goToPage '/save/SubEntity?createTransient=true&object.name=sub1'
                storeXPath xpath:'/html/body/div/div[2]/div/div[3]/div[2]/form/fieldset/div[4]/div/input/@value', property:'id2'

                goToPage '/edit/SubEntity/#{id2}'
                verifyText 'bar'
                verifySelectField name:'object.daEntity', value:''

                setSelectField name:'object.daEntity', text:"ewr1"
                clickButton name:'save'
                verifySelectField name:'object.daEntity', text:'ewr1'

            } finally {
                goToPage '/delete/EntityWithRelations/#{id1}?facet.confirm=true'
                goToPage '/delete/SubEntity/#{id2}?facet.confirm=true'
            }
        }
    }

    void testEnumEdit() {
        webtest("testEnumEdit") {
            login()
            try {

                goToPage '/save/MyBook?createTransient=true&object._id=98765&object.name=Moby&object.nbPages=123'

                // view
                goToPage '/view/MyBook/98765'
                // assert enum is displayed if not null
                verifyText 'initializedRating'
                verifyText 'GOOD'

                // edit
                goToPage '/edit/MyBook/98765'
                verifySelectField name:'object.rating', value:'' // check it's nullable
                verifySelectField name:'object.initializedRating', value:'GOOD' // check it's initialized

                setSelectField name:'object.rating', value:'POOR'
                setSelectField name:'object.initializedRating', value:'AMAZING'
                clickButton name:'save'
                verifyText 'POOR'
                verifyText 'AMAZING'


                // TODO set select check object is saved

            } finally {
                goToPage '/delete/MyBook/98765?facet.confirm=true'
            }
        }
    }

    void testTableDisplayInList() {
        webtest("tableDisplayInList") {
            login()
            try {
                goToPage '/save/MyEntity?createTransient=true&object.id=887766&object.prop1=abc&object.prop2=123'
                goToPage '/list/MyEntity'

                // Verify title
                verifyXPath xpath: '/html/body/div/div[2]/div/h1', text: 'TestPageHeaderTitleOverride'

                // assert some of the DOM
                verifyXPath xpath:'/html/body/div/div[2]/div/table/thead/tr/th', text:'.*Class.*', regex: true
                verifyXPath xpath:'/html/body/div/div[2]/div/table/tbody/tr/td', text:'.*test.MyEntity.*', regex: true
                verifyXPath xpath:'/html/body/div/div[2]/div/table/tbody/tr/td[2]/span/span', text: '.*887766.*', regex: true

                // check the links are present
                verifyXPath xpath:'/html/body/div/div[2]/div/table/tbody/tr/td[5]/div/a'
                verifyXPath xpath:'/html/body/div/div[2]/div/table/tbody/tr/td[5]/div/a[2]'
                verifyXPath xpath:'/html/body/div/div[2]/div/table/tbody/tr/td[5]/div/a[3]'
                verifyXPath xpath:'/html/body/div/div[2]/div/table/tbody/tr/td[5]/div/a[4]'

                // verify the page header title is changed
                verifyText text: "TestPageHeaderTitleOverride"

                logout()
                goToPage '/list/MyEntity'

                // Verify title
                verifyXPath xpath: '/html/body/div/div[2]/div/h1', text: 'TestPageHeaderTitleOverride'

                // there should be no more "class" field
                verifyText text: '.*887766.*', regex: true
                not {
                    verifyText text:'.*test.MyEntity.*', regex: true
                }

                // only a "view" button
                verifyXPath xpath:'/html/body/div/div[2]/div/table/tbody/tr/td[4]/div/a'
                not {
                    verifyXPath xpath:'/html/body/div/div[2]/div/table/tbody/tr/td[4]/div/a[2]'
                }

            } finally {
                goToPage '/delete/MyEntity/887766?facet.confirm=true'
            }
        }
    }

    void testRenderLinkAttributes() {
        webtest("testRenderLinkAttributes") {
            login()
            try {

                goToPage '/save/MyBook?createTransient=true&object._id=11221122&object.name=Moby&object.nbPages=123'

                // close edit link with attribute
                clickLink xpath:"/html/body/div/div[2]/div/div[3]/div/div[2]/div/a[@testmeedit='11221122']"

                // edit with link attribute
                clickLink xpath:"/html/body/div/div[2]/div/div[3]/div/div[2]/div/a[@testme='11221122']"
            } finally {
                goToPage '/delete/MyBook/11221122?facet.confirm=true'
            }
        }
    }


}

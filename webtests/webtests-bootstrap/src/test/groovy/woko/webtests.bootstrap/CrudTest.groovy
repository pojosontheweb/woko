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

class CrudTest extends WebTestBase {

    void testCrud() {
        webtest('CRUD operations') {
            login()

            // create
            goToPage '/save/MyBook?object._id=1&object.name=Moby'
            verifyText 'Object saved'
            verifyXPath xpath: "/html/body/div/div[2]/div/div/div[2]/div[2]/div/form/fieldset/div[4]/div/input[@value='Moby']"
            // TODO verifyTitle 'Woko - Moby'

            // view
            goToPage '/view/MyBook/1'
            verifyXPath xpath: "/html/body/div/div[2]/div/div/div/div[2]/div/div[4]/div[2]/span/span"
            text: 'Moby'

            // update
            goToPage '/save/MyBook?object._id=1&object.name=Mobyz'
            verifyText 'Object saved'
            verifyXPath xpath: "/html/body/div/div[2]/div/div/div[2]/div[2]/div/form/fieldset/div[4]/div/input[@value='Mobyz']"
            // TODO verifyTitle 'Woko - Mobyz'

            // view
            goToPage '/view/MyBook/1'
            verifyXPath xpath: "/html/body/div/div[2]/div/div/div/div[2]/div/div[4]/div[2]/span/span"
            text: 'Mobyz'

            // delete
            goToPage '/delete/MyBook/1'
            // TODO verifyTitle 'Woko - Mobyz'
            verifyText 'Please confirm deletion'
            clickButton name: 'facet.confirm'
            verifyText 'Object deleted'

            not {
                goToPage '/view/MyBook/1'
            }
        }
    }


    void test404() {
        webtest('404') {
            login()
            not {
                goToPage '/idontexist/MyBook/1'
            }
            not {
                goToPage '/view/MyBookZZZ/1'
            }
            not {
                goToPage '/view/MyBook/999999999999'
            }
            not {
                goToPage '/view/MyBookZZZ/99999999'
            }
        }
    }

    void testValidationOnSave() {
        webtest('Validation on Save') {
            login()
            goToPage '/save/MyBook'
            verifyText 'Name is a required field'

            clickButton name: 'save'
            verifyText 'Name is a required field'

            setInputField name: 'object._id', value: '2'
            setInputField name: 'object.name', value: 'Moby Dick'
            clickButton name: 'save'
            verifyText 'Object saved'

            // delete the object to avoid influencing other tests
            goToPage '/delete/MyBook/2?facet.confirm=true'
        }
    }

    // fails because the id is repopulated by Stripes, thereby it's passed
    // and woko tries to load the object instead of creating a new one...
    // maybe we should delegate object creation to a facet in case the loader returns null
    // TODO rebranch when we know what it's used for !
    /*
    void testValidationOnSaveWithIdProvided() {
      webtest('Validation on Save with ID provided') {
        login()
        goToPage '/save/MyBook'
        verifyText 'Please fix the following errors'
        verifyXPath xpath:"/html/body/div/div[3]/div[2]/div[2]/div/form/table/tbody/tr[4]/td/span/span/input[@class='error']"

        setInputField name:'object._id', value:'3'
        clickButton name:'save'
        verifyText 'Please fix the following errors'
        verifyXPath xpath:"/html/body/div/div[3]/div[2]/div[2]/div/form/table/tbody/tr[4]/td/span/span/input[@class='error']"

        setInputField name:'object.name', value:'Moby Dick'
        clickButton name:'save'
        verifyText 'Object saved'

        // delete the object to avoid influencing other tests
        goToPage '/delete/MyBook/3?facet.confirm=true'
      }
    }
    */

    void testFindAndList() {
        webtest('test Find and List') {
            login()
            // create some test books...
            goToPage "/createDummyObjects"

            try {
                // find
                goToPage '/find'
                // TODO verifyTitle 'Woko - Find objects'
                verifyText 'Find objects by class'
                clickLink label: 'MyBook'

                verifyText '400 object(s) found for class MyBook'
                verifyXPath xpath: "/html/body/div/div[2]/div/div/div[2]/div/ul/li[11]/a" // check that link to page 10 exists
                verifyText 'Moby test100'
                clickLink xpath: '/html/body/div/div[2]/div/div/div[2]/div/ul/li[3]/a' // click page 2
                verifyText 'Moby test110'
                clickLink xpath: '/html/body/div/div[2]/div/div/div[2]/div/ul/li[4]/a' // click page 3
                verifyText 'Moby test120'
                clickLink xpath: '/html/body/div/div[2]/div/div/div[2]/div/ul/li[12]/a' // click "next"
                verifyText 'Moby test130'


                setSelectField name: 'facet.resultsPerPage', value: '500'
                verifyText 'Moby test499'
                not {
                    verifyXPath xpath: "//div[@class='pagination']"
                }

                // list
                goToPage '/list/MyBook'
                verifyText '400 object(s) found for class MyBook'
            } finally {
                // remove the objects
                goToPage "/removeDummyObjects"
            }
        }
    }

    void testSearch() {
        webtest('test Search') {
            login()
            // create some test books...
            goToPage "/createDummyObjects"
            verifyText 'all good'

            goToPage "/search"
            setInputField xpath: '/html/body/div/div[2]/div/div/div/form/input', value: 'moby'
            clickButton name: 'search'

            verifyText '400 object(s) found'

            verifyXPath xpath: "/html/body/div/div[2]/div/div/div[3]/div/ul/li[11]/a" // check that link to page 10 exists
            verifyText 'Moby test100'
            clickLink xpath: '/html/body/div/div[2]/div/div/div[3]/div/ul/li[3]/a' // click page 2
            verifyText text: 'Moby test'
            clickLink xpath: "/html/body/div/div[2]/div/div/div[3]/div/ul/li[4]/a" // click page 3
            verifyText text: 'Moby test'
            clickLink xpath: '/html/body/div/div[2]/div/div/div[3]/div/ul/li[12]/a' // click "next"
            verifyText text: 'Moby test'

            setSelectField xpath: '/html/body/div/div[2]/div/div/div[2]/form/select', value: '500'
            verifyText 'Moby test'
            not {
                verifyXPath xpath: "//div[@class='pagination']"
            }

            // list
            goToPage '/search?facet.query=moby'
            verifyText '400 object(s) found'

            // remove the objects
            goToPage "/removeDummyObjects"
        }

    }

    void testAssociations() {
        webtest('testAssociations') {
            login()

            // save a EntityWithRelations object for our tests
            goToPage '/save/EntityWithRelations?object.name=test'

            // click links and check associations
            goToPage '/create'
            setSelectField name: 'className', value: 'SubEntity'
            clickButton name: 'create'

            setInputField name: "object.name", value: "testAssociate"
            setSelectField name: "object.daEntity", text: "test"
            clickButton name: 'save'
            verifyText 'Object saved'

            setSelectField name: "object.children", text: "test"
            clickButton name: 'save'
            verifyText 'Object saved'

            clickLink label: 'Close editing'
            verifyXPath xpath: '/html/body/div/div[2]/div/div/div/div[2]/div/div/div[2]/span/span/div/span/span/a',
                    text: 'test'
            verifyXPath xpath: '/html/body/div/div[2]/div/div/div/div[2]/div/div[3]/div[2]/span/span/a',
                    text: 'test'
        }
    }


}

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

class CrudTest extends WebTestBase {

  void testCrud() {
    webtest('CRUD operations') {
      login()

      // create
      goToPage '/save/MyBook?object._id=1&object.name=Moby'
      verifyText 'Object saved'
      verifyXPath xpath:"/html/body/div/div[3]/div/div/div/form/table/tbody/tr[4]/td/span/span/input[@value='Moby']"
      verifyTitle 'Woko - Moby'

      // view
      goToPage '/view/MyBook/1'
      verifyXPath xpath:"/html/body/div/div[3]/div/div/div/table/tbody/tr[4]/td/span/span"
        text:'Moby'

      // update
      goToPage '/save/MyBook?object._id=1&object.name=Mobyz'
      verifyText 'Object saved'
      verifyXPath xpath:"/html/body/div/div[3]/div/div/div/form/table/tbody/tr[4]/td/span/span/input[@value='Mobyz']"
      verifyTitle 'Woko - Mobyz'

      // view
      goToPage '/view/MyBook/1'
      verifyXPath xpath:"/html/body/div/div[3]/div/div/div/table/tbody/tr[4]/td/span/span"
        text:'Mobyz'

      // delete
      goToPage '/delete/MyBook/1'
      verifyTitle 'Woko - Mobyz'
      verifyText 'Please confirm deletion'
      clickButton name:'facet.confirm'
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
      verifyText 'Please fix the following errors'

      clickButton name:'save'
      verifyText 'Please fix the following errors'

      setInputField name:'object._id', value:'2'
      setInputField name:'object.name', value:'Moby Dick'
      clickButton name:'save'
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
        verifyTitle 'Woko - Find objects'
        verifyText 'Find objects by class'
        clickLink label:'MyBook'

        verifyText '400 object(s) found for class MyBook'
        verifyXPath xpath: "/html/body/div/div[3]/div/div[2]/span[40]/a" // 40 pages
        verifyText 'Moby test100'
        clickLink label:'2'
        verifyText 'Moby test110'
        clickLink label:'40'
        verifyText 'Moby test499'
        not {
          // check that we don't have a link to 41st page (should not exist)
          verifyXPath xpath: '/html/body/div/div[3]/div/div[2]/span[41]/a'
        }

        setSelectField name:'facet.resultsPerPage', value:'500'
        verifyText 'Moby test499'
        not {
          verifyXPath xpath:"//div[@class='wokoPagination']"
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

      // full text search from top-right box
      goToPage "/search"
      setInputField name:'facet.query', value:'moby'
      clickButton name:'search'

      verifyText '400 object(s) found'
      verifyXPath xpath: "/html/body/div/div[3]/div/div[3]/span[40]/a" // link to 40th page
      verifyText 'Moby test100'
      clickLink label:'2'
      verifyText text:'Moby test1[0-9][0-9]', regex:true
      clickLink label:'40'
      verifyText text:'Moby test4[0-9][0-9]', regex:true
      not {
        verifyXPath xpath: "/html/body/div/div[3]/div/div[3]/span[41]/a" // link to 41th page should not exist
      }

      setSelectField name:'facet.resultsPerPage', value:'500'
      verifyText 'Moby test499'
      not {
        verifyXPath xpath:"//div[@class='wokoPagination']"
      }

      // list
      goToPage '/search?facet.query=moby'
      verifyText '400 object(s) found'

      // remove the objects
      goToPage "/removeDummyObjects"
    }

  }


}

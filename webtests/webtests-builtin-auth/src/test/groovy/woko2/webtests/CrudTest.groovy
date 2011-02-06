package woko2.webtests

class CrudTest extends WebTestBase {

  void testCrud() {
    webtest('CRUD operations') {
      login()

      // create
      goToPage '/save/MyBook?object._id=1&object.name=Moby'
      verifyText 'Object saved'
      verifyXPath xpath:"/html/body/div/div[3]/div[2]/div/div/form/table/tbody/tr[4]/td/span/span/input[@value='Moby']"
      verifyTitle 'Woko - Moby'

      // view
      goToPage '/view/MyBook/1'
      verifyXPath xpath:"/html/body/div/div[3]/div[2]/div/div/table/tbody/tr[4]/td/span/span"
        text:'Moby'

      // update
      goToPage '/save/MyBook?object._id=1&object.name=Mobyz'
      verifyText 'Object saved'
      verifyXPath xpath:"/html/body/div/div[3]/div[2]/div/div/form/table/tbody/tr[4]/td/span/span/input[@value='Mobyz']"
      verifyTitle 'Woko - Mobyz'

      // view
      goToPage '/view/MyBook/1'
      verifyXPath xpath:"/html/body/div/div[3]/div[2]/div/div/table/tbody/tr[4]/td/span/span"
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

  void testValidationOnSave() {
    webtest('Validation on Save') {
      login()
      goToPage '/save/MyBook'
      verifyText 'Please fix the following errors'
      verifyXPath xpath:"/html/body/div/div[3]/div[2]/div[2]/div/form/table/tbody/tr[4]/td/span/span/input[@class='error']"

      clickButton name:'save'
      verifyText 'Please fix the following errors'
      verifyXPath xpath:"/html/body/div/div[3]/div[2]/div[2]/div/form/table/tbody/tr[4]/td/span/span/input[@class='error']"

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

  void testFindAndList() {
    webtest('test Find and List') {
      login()
      // create some test books...
      for (int i=100 ; i<500 ; i++) {
        goToPage "/save/MyBook?object._id=$i&object.name=Moby$i"
        verifyText 'Object saved'
      }

      // find
      goToPage '/find'
      verifyTitle 'Woko - Find objects'
      verifyText 'Find objects by class'
      clickLink label:'MyBook'

      verifyText '400 object(s) found for class MyBook'
      verifyXPath xpath: "/html/body/div/div[3]/div[2]/div[2]/span[40]/a" // 40 pages
      verifyText 'Moby100'
      clickLink label:'2'
      verifyText 'Moby110'
      clickLink label:'40'
      verifyText 'Moby499'

      setSelectField name:'facet.resultsPerPage', value:'500'
      verifyText 'Moby499'
      not {
        verifyXPath xpath:"//div[@class='wokoPagination']"
      }

      // list
      goToPage '/list/MyBook'
      verifyText '400 object(s) found for class MyBook'

      // remove the objects
      for (int i=100 ; i<500 ; i++) {
        goToPage "/delete/MyBook?/$i&facet.confirm=true"
        verifyText 'Object deleted'
      }
    }
  }

  void testSearch() {
    webtest('test Search') {
      login()
      // create some test books...
      for (int i=100 ; i<500 ; i++) {
        goToPage "/save/MyBook?object._id=$i&object.name=Moby test$i"
        verifyText 'Object saved'
      }

      // full text search from top-right box
      setInputField name:'facet.query', value:'moby'
      clickButton name:'search'

      verifyText '400 object(s) found'
      verifyXPath xpath: "/html/body/div/div[3]/div[2]/div[3]/span[40]/a" // link to 40th page
      verifyText 'Moby test100'
      clickLink label:'2'
      verifyText 'Moby test110'
      clickLink label:'40'
      verifyText 'Moby test499'

      setSelectField name:'facet.resultsPerPage', value:'500'
      verifyText 'Moby test499'
      not {
        verifyXPath xpath:"//div[@class='wokoPagination']"
      }

      // list
      goToPage '/search?facet.query=moby'
      verifyText '400 object(s) found'

      // remove the objects
      for (int i=100 ; i<500 ; i++) {
        goToPage "/delete/MyBook?/$i&facet.confirm=true"
        verifyText 'Object deleted'
      }
    }

  }


}

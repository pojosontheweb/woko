package woko.webtests.localization

/**
 * Test the UI of the confirmDelete jsp.<br/>
 * Feature only available for devel user
 */
class ConfirmDeletePageTest extends WebTestBase {

  void testConfirmDelete() {
    webtest("test confimDelete") {
      login()

      // Create an object first
      goToPage '/save/MyBook?object._id=1&object.name=Moby'

      // Got to ConfirmDelete
      goToPage '/delete/MyBook/1'

      verifyTitle 'Woko - Moby'
      verifyText 'Please confirm deletion'
      verifyText 'You are about to permanently delete object "Moby". Are you sure ?'

      verifyXPath xpath:"/html/body/div/div[3]/div/form[@action='/woko-webtests/delete/MyBook/1']"
      verifyXPath xpath:"/html/body/div/div[3]/div/form/input[1][@value='Delete']"
      verifyXPath xpath:"/html/body/div/div[3]/div/form/input[1][@name='facet.confirm']"
      verifyXPath xpath:"/html/body/div/div[3]/div/form/input[2][@value='Cancel']"
      verifyXPath xpath:"/html/body/div/div[3]/div/form/input[2][@name='facet.cancel']"

      // Delete the object to avoid influencing other tests
      clickButton name:'facet.confirm'
      
    }
  }
}

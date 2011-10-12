package woko.webtests.localization

import woko.webtests.WebTestBase

/**
 * @author Alexis Boissonnat - alexis.boissonnat 'at' gmail.com
 */
class SavePageTest extends WebTestBase{

    void testSavePage(){
        webtest('test Save page'){
            login()

            goToPage '/save/MyBook'

            verifyXPath xpath: "/html/body/div/div[3]/div/div[1]/span", text:'Please fix the following errors:'

            // Check if the 'cannot be null' hibernate error has been transformed in 'FieldName is a required field' Stripes error
//            verifyXPath xpath: "/html/body/div/div[3]/div/div[1]/span/ul/li", text:'MyBook.name is a required field'
        }
    }
}

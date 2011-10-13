package woko.webtests.localization

import woko.webtests.WebTestBase

/**
 * @author Alexis Boissonnat - alexis.boissonnat 'at' gmail.com
 */
class SavePageTest extends WebTestBase{

    void testSavePage(){
        webtest('test Save page'){
            login()

            goToPage '/save/MyJavaEntity'

            verifyXPath xpath: "/html/body/div/div[3]/div/div[1]/span", text:'Please fix the following errors:'

            // Check if the 'cannot be null' hibernate error has been transformed in 'FieldName is a required field' Stripes error
            verifyXPath xpath: "/html/body/div/div[3]/div/div[1]/ul/li[1]", text:'The minimum allowed value for Object Min Number is 2'
            verifyXPath xpath: "/html/body/div/div[3]/div/div[1]/ul/li[2]", text:'Name is a required field'
            verifyXPath xpath: "/html/body/div/div[3]/div/div[1]/ul/li[3]", text:'Object Range must be between 2 and 10'
        }
    }
}

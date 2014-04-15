package woko.webtests.bootstrap3

import org.junit.Test
import org.pojosontheweb.selenium.formz.Select

class Defect203Test extends WebTestBase {

    @Test
    void testDefect203(){
        login()
        clickLink "create"
        new Select(byName("className")).selectByVisibleText("Defect203Entity")
        byName("create").click()
        byName("object.intProp").sendKeys("abcdef")
        byName("save").click()
        verifyText "The value (abcdef) entered in field Defect203 Entity Int Prop must be a valid number"
    }

}

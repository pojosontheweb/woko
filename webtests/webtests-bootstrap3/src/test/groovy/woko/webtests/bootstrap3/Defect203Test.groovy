package woko.webtests.bootstrap3

import org.junit.Test
import com.pojosontheweb.selenium.formz.Select
import woko.webtests.SeleniumTestBase

class Defect203Test extends SeleniumTestBase {

    @Test
    void testDefect203(){
        login()
        clickLink "create"
        Select.selectByVisibleText(byName("className"), "Defect203Entity")
        byName("create").click()
        byName("object.intProp").sendKeys("abcdef")
        byName("save").click()
        verifyText "The value (abcdef) entered in field Defect203 Entity Int Prop must be a valid number"
    }

}

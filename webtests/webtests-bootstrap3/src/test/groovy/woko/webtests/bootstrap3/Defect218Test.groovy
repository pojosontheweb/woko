package woko.webtests.bootstrap3

import com.pojosontheweb.selenium.Findrs
import org.junit.Test
import woko.webtests.SeleniumTestBase

class Defect218Test extends SeleniumTestBase {

    @Test
    void testDefect218(){
        goToPage '/utf8test'
        [
            msgWokoApi: "rémi",
            msgWokoTag: "rémi",
            msgWokoTagWithArg: "rémi allôôô ouééééé"
        ].each { k,v ->
            byId(k).where(Findrs.textEquals(v)).eval()
        }
    }

}

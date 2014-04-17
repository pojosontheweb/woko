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

package woko.webtests.bootstrap3

import org.junit.Test
import org.openqa.selenium.By
import com.pojosontheweb.selenium.Findr
import com.pojosontheweb.selenium.formz.Select

class CrudTest extends WebTestBase {

    @Test
    void testCrud() {
        login()

        // create
        goToPage '/save/MyBook?createTransient=true&object._id=1&object.name=Moby'
        verifyText 'Object saved'
        verifyXPath "//form//input[@value='Moby']"

        // view
        goToPage '/view/MyBook/1'
        verifyXPath "/html/body/div[2]/div[2]/div[6]/div[2]/p"
                    text: 'Moby'

        // update
        goToPage '/save/MyBook/1?object.name=Mobyz'
        verifyText 'Object saved'
        verifyXPath "/html/body/div[3]/form/div[1]/div[6]/div[2]/div/div/input[@value='Mobyz']"

        // view
        goToPage '/view/MyBook/1'
        verifyXPath "/html/body/div[2]/div[2]/div[6]/div[2]/p"
                    text: 'Mobyz'

        // delete
        goToPage '/delete/MyBook/1'
        verifyText 'Please confirm deletion'
        findr().elem(By.name("facet.confirm")).click()
        verifyText 'Object deleted'

        not {
            goToPage '/view/MyBook/1'
        }
    }

    @Test
    void test404() {
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

    @Test
    void testValidationOnSave() {
        login()
        goToPage '/save/MyBook?createTransient=true'
        verifyText 'Name may not be null'

        byName("save").click()
        verifyText 'Name may not be null'

        byName('object._id').sendKeys('2')
        byName('object.name').sendKeys('Moby Dick')
        try {
            byName('save').click()
            verifyText 'Object saved'
        } finally {
            // delete the object to avoid influencing other tests
            goToPage '/delete/MyBook/2?facet.confirm=true'
        }
    }

    @Test
    void testFindAndList() {
        login()
        // create some test books...
        goToPage "/createDummyObjects"

        try {
            // find
            goToPage '/find'
            verifyText 'Find objects by class'
            clickLink("MyBook")

            verifyText '400 object(s) found for class MyBook'
            verifyXPath "/html/body/div[2]/ul/li[11]/a" // check that link to page 10 exists
            verifyText 'Moby test100'

            byXpath('/html/body/div[2]/ul/li[3]/a').click() // click page 2
            verifyText 'Moby test110'
            byXpath('/html/body/div[2]/ul/li[4]/a').click() // click page 3
            verifyText 'Moby test120'
            byXpath('/html/body/div[2]/ul/li[12]/a').click() // click "next"
            verifyText 'Moby test130'

            Select.selectByVisibleText(byName("facet.resultsPerPage"), "500");

            verifyText 'Moby test499'
            not {
                verifyXPath "//ul[@class='pagination']"
            }

            // list
            goToPage '/list/MyBook'
            verifyText '400 object(s) found for class MyBook'
        } finally {
            // remove the objects
            goToPage "/removeDummyObjects"
        }
    }

    @Test
    void testSearch() {
        login()
        // create some test books...
        goToPage "/createDummyObjects"
        verifyText 'all good'

        try {
            goToPage "/search"
            byXpath('/html/body/div[2]/div[1]/div[1]/form/div[1]/input').sendKeys('moby')
            byXpath('/html/body/div[2]/div[1]/div[1]/form/div[1]/span/button').click()

            verifyText '400 object(s) found'
            verifyXPath "/html/body/div[2]/ul/li[11]/a" // check that link to page 10 exists
            verifyText 'Moby test100'
            byXpath('/html/body/div[2]/ul/li[3]/a').click() // click page 2
            verifyText 'Moby test'
            byXpath('/html/body/div[2]/ul/li[4]/a').click() // click page 3
            verifyText 'Moby test'
            byXpath('/html/body/div[2]/ul/li[12]/a').click() // click "next"
            verifyText 'Moby test'

            Select.selectByVisibleText(byName("facet.resultsPerPage"), "500")

            verifyText 'Moby test'
            not {
                verifyXPath "//ul[@class='pagination']"
            }

            // list
            goToPage '/search?facet.query=moby'
            verifyText '400 object(s) found'

        } finally {
            // remove the objects
            goToPage "/removeDummyObjects"
        }

    }

    @Test
    void testAssociations() {
        login()

        try {

            goToPage '/save/EntityWithRelations?createTransient=true&object.name=test&object.id=1'
            goToPage '/save/SubEntity?createTransient=true&object.name=testAssociate&object.id=2'

            // click links and check associations
            goToPage '/edit/SubEntity/2'
            Select.selectByVisibleText(byName("object.daEntity"), "test")
            byName('save').click()
            verifyText 'Object saved'

            Select.selectByVisibleText(byName("object.children"), "test")
            byName("save").click()
            verifyText 'Object saved'

            clickLink "Close editing"
            byXpath('/html/body/div[2]/div[2]/div[3]/div[2]/a')
                    .where(Findr.textEquals('test'))
                    .eval()
        } finally {
            goToPage '/delete/EntityWithRelations/1?facet.confirm=true'
            goToPage '/delete/SubEntity/2?facet.confirm=true'
        }
    }


}

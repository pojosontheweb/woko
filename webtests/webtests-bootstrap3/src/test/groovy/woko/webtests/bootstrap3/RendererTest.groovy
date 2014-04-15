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

import org.junit.Assert
import org.junit.Ignore
import org.junit.Test
import org.pojosontheweb.selenium.Findr
import org.pojosontheweb.selenium.formz.Select

class RendererTest extends WebTestBase {

    @Test
    void testPropertyValueFacetOverrides() {
        login()
        // create test object
        goToPage '/save/MyBook?createTransient=true&object._id=1111&object.name=Moby&object.nbPages=123'

        // view
        goToPage '/view/MyBook/1111'
        verifyText '123 page(s)' // view override

        // edit
        goToPage '/edit/MyBook/1111'
        verifyText 'page(s)'

        // check that buttons have been overriden (renderPropertiesEditButtons)
        verifyText 'Funky button'

        goToPage '/delete/MyBook/1111?facet.confirm=true'
    }

    @Test
    void testReadOnlyPropCannotBeEdited() {
        login()
        try {
            goToPage '/save/MyEntityWithReadOnlyProp?createTransient=true&object.id=1234'
            verifyText "readonlyvalue"
            not {
                verifyXPath "//input[@name=object.readOnlyProp]"
            }
        } finally {
            goToPage '/delete/MyEntityWithReadOnlyProp/1234?facet.confirm=true'
        }
    }

    @Test
    void testFlatLayout() {
        login()
        goToPage '/save/MyEntity?createTransient=true&object.id=5566&object.prop1=abc&object.prop2=123'
        try {
            goToPage '/view/MyEntity/5566'
            byXpath('/html/body/div[2]/div[4]/div/div/p')
                    .where(Findr.textEquals('abc'))
                    .eval()
        } finally {
            goToPage '/delete/MyEntity/5566?facet.confirm=true'
        }
    }

    @Test
    void testComboForPersistentXToOne() {
        login()
        try {
            goToPage '/save/EntityWithRelations?createTransient=true&object.name=ewr1&object.id=1'
            goToPage '/save/SubEntity?createTransient=true&object.name=sub1&object.id=1'

            goToPage '/edit/SubEntity/1'
            def daEntitySelect = new Select(byName('object.daEntity'))
            daEntitySelect.assertSelectedText('')

            daEntitySelect.selectByVisibleText("ewr1")
            byName("save").click()
            daEntitySelect.assertSelectedText('ewr1')

        } finally {
            goToPage '/delete/EntityWithRelations/1?facet.confirm=true'
            goToPage '/delete/SubEntity/1?facet.confirm=true'
        }
    }

    @Test
    @Ignore
    void testMultiSelectForPersistentXToMany() {
        Assert.fail("TODO")
    }

        @Test
    void testEnumEdit() {
        login()
        try {

            goToPage '/save/MyBook?createTransient=true&object._id=98765&object.name=Moby&object.nbPages=123'

            // view
            goToPage '/view/MyBook/98765'
            // assert enum is displayed if not null
            verifyText 'initializedRating'
            verifyText 'GOOD'

            // edit
            goToPage '/edit/MyBook/98765'

            def selectRating = new Select(byName("object.rating"))
            selectRating.assertSelectedText("") // check it's nullable
            def selectInit = new Select(byName('object.initializedRating'))
            selectInit.assertSelectedText('GOOD') // check it's initialized

            selectRating.selectByVisibleText('POOR')
            selectInit.selectByVisibleText('AMAZING')
            byName("save").click()
            verifyText 'POOR'
            verifyText 'AMAZING'

        } finally {
            goToPage '/delete/MyBook/98765?facet.confirm=true'
        }
    }

    @Test
    void testTableDisplayInList() {
        login()
        try {
            goToPage '/save/MyEntity?createTransient=true&object.id=887766&object.prop1=abc&object.prop2=123'
            goToPage '/list/MyEntity'

            // Verify title
            verifyText 'TestPageHeaderTitleOverride - Should be displayed only on load'

            // assert some of the DOM
            byXpath('/html/body/div[2]/table/thead/tr/th[1]').where(Findr.textEquals('Class')).eval()
            byXpath('/html/body/div[2]/table/tbody/tr/td[1]/p').where(Findr.textEquals('test.MyEntity')).eval()
            byXpath('/html/body/div[2]/table/tbody/tr/td[2]/p').where(Findr.textEquals('887766')).eval()

            // check the links are present
            verifyXPath '/html/body/div[2]/table/tbody/tr/td[5]/div/a[1]'
            verifyXPath '/html/body/div[2]/table/tbody/tr/td[5]/div/a[2]'
            verifyXPath '/html/body/div[2]/table/tbody/tr/td[5]/div/a[3]'
            verifyXPath '/html/body/div[2]/table/tbody/tr/td[5]/div/a[4]'

            // verify the page header title is changed
// TODO nothing clicked here, text cannot change
// verifyText text: "TestPageHeaderTitleOverride"

            logout()
            goToPage '/list/MyEntity'

            // Verify title
            verifyText 'TestPageHeaderTitleOverride'

            // there should be no more "class" field
            verifyText '887766'
            not {
                verifyText 'test.MyEntity'
            }

            // only a "view" button
            verifyXPath '/html/body/div[2]/table/tbody/tr/td[4]/div/a'
            not {
                verifyXPath '/html/body/div[2]/table/tbody/tr/td[4]/div/a[2]'
            }

        } finally {
            goToPage '/delete/MyEntity/887766?facet.confirm=true'
        }
    }

    @Test
    void testRenderLinkAttributes() {
        login()
        try {

            goToPage '/save/MyBook?createTransient=true&object._id=11221122&object.name=Moby&object.nbPages=123'

            // close edit link with attribute
            byXpath("/html/body/div[3]/div/div/div[2]/div/div/a[1][@testmeedit='11221122']").click()

            // edit with link attribute
            byXpath("/html/body/div[2]/div[1]/div/div[2]/div/div/a[1][@testme='11221122']").click()
        } finally {
            goToPage '/delete/MyBook/11221122?facet.confirm=true'
        }
    }

}

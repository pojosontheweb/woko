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

package woko.webtests.rpc

import org.junit.Test
import org.junit.runners.JUnit4
import org.junit.runner.RunWith
import org.junit.Ignore

@RunWith(JUnit4.class)
class RpcTest extends WebTestBase {

    static final String RESULT_START = """{"_id":"1111","name":"Moby","class":"MyBook","creationTime":"""
    static final String RESULT_END = ""","_wokoInfo":{"title":"Moby","className":"MyBook","key":"1111"},"nbPages":{"nbPagesXXX":123}}"""

    @Test
    void createGetDelete() {
        webtest('test RPC create / get / delete') {
            login()

            // create test object
            goToPage '/save/MyBook?object._id=1111&object.name=Moby&object.nbPages=123&isRpc=true'
            verifyText RESULT_START
            verifyText RESULT_END

            // view
            goToPage '/view/MyBook/1111?isRpc=true'
            verifyText RESULT_START
            verifyText RESULT_END

            // delete
            goToPage '/delete/MyBook/1111?facet.confirm=true&isRpc=true'
            verifyText '{ "success": true }'

            // verify /view doesn't work any more
            not {
                goToPage '/view/MyBook/1111&isRpc=true'
            }
        }
    }

    @Test
    void listAndSearch() {
        webtest('test RPC list and search') {
            login()

            // create a bunch of objects
            goToPage '/createTestObjects'
            verifyText '{success:true}'

            // list
            goToPage '/list/MyBook?isRpc=true'
            verifyText """"totalSize":10"""
            verifyText """"start":0"""
            verifyText """"limit":10"""
            verifyText """Moby1"""

            // search
            goToPage '/search?facet.query=moby*&isRpc=true'
            verifyText """"totalSize":10"""
            verifyText """"start":0"""
            verifyText """"limit":10"""
            verifyText """Moby1"""

            // remove all
            goToPage '/deleteTestObjects'
            verifyText '{success:true}'
        }
    }

    @Test
    void javaScriptAPIDojo() {
        webtest('test JS api Dojo') {
            login()
            goToPage("/testRpc.html");
            retry(maxcount: 10) {
                verifyText "loaded"
                verifyText "Save1"
                verifyText "Reload"
                verifyText "Save1"
                verifyText "Deleted"
                verifyText "DateOK"
                verifyText "removedowd"
                sleep 1
            }

            // TODO those tests are too long : find another testing method !
            /*
            goToPage("/testRpcFind.html");
            retry(maxcount: 10) {
                println "Trying..."
                verifyText "list1 returned 3 items on 100"
                verifyText "list2 returned 3 items on 100"
                verifyText "test list scroll OK"
                verifyText "Removed test objects"
            }

            goToPage("/testRpcSearch.html");
            retry(maxcount: 200) {
                verifyText "search returned 1 items on 1"
                verifyText "Removed test objects"
                sleep 1000
            }
            */
        }
    }

    @Test
    void testExceptionIsSerializedAsJson() {
        webtest("testExceptionIsSerializedAsJson") {
            config {
                option name:"ThrowExceptionOnFailingStatusCode", value:false
            }
            goToPage "/throw?isRpc=true"
            storeResponseCode property:"status"
            verifyProperty name:"status", text:"500"
            verifyText "ouch !"
            verifyText "error\":true"
            verifyText "ticket"

            goToPage "/IdontExIsT?isRpc=true"
            storeResponseCode property:"status"
            verifyProperty name:"status", text:"404"
            verifyText "requested resource not found"
            verifyText "error\":true"
            verifyText "ticket"
        }
    }

    @Test
    void testValidationErrorsAreSerializedAsJson() {
        webtest("testValidationErrorsAreSerializedAsJson") {
            goToPage "/validationInThere?isRpc=true"
            verifyText "Validation errors"
            verifyText "error\":true"
            verifyText "validation\":true"
            verifyText "fieldName\":\"facet.myProp"
        }
    }

    @Test
    @Ignore
    // TODO jquery /webtest issue here...
    void javaScriptAPIJQuery() {
        webtest('test JS api JQuery') {
            login()
            goToPage("/testRpcJquery.html");
            retry(maxcount: 50) {
                verifyText "loaded"
                verifyText "Save1"
                verifyText "Reload"
                verifyText "Save1"
                verifyText "Deleted"
                verifyText "DateOK"
                verifyText "removedowd"
                sleep 1
            }
        }
    }

}

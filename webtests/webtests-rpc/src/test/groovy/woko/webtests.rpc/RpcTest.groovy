package woko.webtests.rpc

import org.junit.Test
import org.junit.runners.JUnit4
import org.junit.runner.RunWith
import org.junit.Ignore

@RunWith(JUnit4.class)
class RpcTest extends WebTestBase {

    static final String RESULT_START = """{"_object":true,"_id":"1111","_persistent":true,"_className":"MyBook","_key":"1111","name":"Moby","class":"MyBook","creationTime"""
    static final String RESULT_END = """"_title":"Moby","nbPages":{"nbPagesXXX":123}}"""

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

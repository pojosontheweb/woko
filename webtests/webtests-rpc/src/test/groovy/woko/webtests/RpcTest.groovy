package woko.webtests

import org.junit.Test
import org.junit.runners.JUnit4
import org.junit.runner.RunWith

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
            verifyText "{success:true}"

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
            verifyText 'OK'

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
            verifyText 'OK'
        }
    }

    @Test
    void javaScriptAPI() {
        webtest('test JS api') {
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
        }
    }

}

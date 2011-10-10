package woko.webtests

class RpcTest extends WebTestBase {

    static final String RESULT_START = """{"_object":true,"_id":"1111","_persistent":true,"_className":"MyBook","_key":"1111","name":"Moby","class":"MyBook","creationTime"""
    static final String RESULT_END = """"_title":"Moby","nbPages":{"nbPagesXXX":123}}"""

    void testCreateGetDelete() {
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
        }
    }


}

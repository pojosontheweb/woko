package woko.webtests.containerauth

class BindingSecurityTest extends WebTestBase {

    void testBindingSecurityOnFacet() {
        webtest("testBindingSecurityOnFacet") {

            login()
            goToPage '/save/MyEntity?object.id=1&object.prop1=val1&object.prop2=shouldnotbind'
            try {

                goToPage '/bindMe/MyEntity/1'
                verifyText 'Binding security tests'

                goToPage '/bindMe/MyEntity/1?facet.alwaysBound=funk2live'
                verifyText 'alwaysBound=funk2live'
                verifyText 'neverBound=shouldnotchange'
                verifyText 'other.foo=bar'

                goToPage '/bindMe/MyEntity/1?facet.alwaysBound=funk2live&facet.neverBound=punkisnotdead'
                verifyText 'alwaysBound=funk2live'
                verifyText 'neverBound=shouldnotchange'
                not {
                    verifyText 'neverBound=punkisnotdead'
                }
                verifyText 'other.foo=bar'

                goToPage '/bindMe/MyEntity/1?other.foo=bazzz'
                verifyText 'other.foo=bar'
                not {
                    verifyText 'other.foo=bazzz'
                }
            } finally {
                goToPage '/delete/MyEntity/1?facet.confirm=true'
            }

        }
    }

    void testBindingSecurityOnObject() {
        webtest("testBindingSecurityOnObject") {

            login()
            goToPage '/save/MyEntity?object.id=1&object.prop1=val1&object.prop2=123456'
            try {

                verifyText 'Object saved'

                goToPage '/view/MyEntity/1'
                not {
                    verifyText '123456'
                }

            } finally {
                goToPage '/delete/MyEntity/1?facet.confirm=true'
            }
        }
    }


}

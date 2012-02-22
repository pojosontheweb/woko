package woko.webtests

class BindingSecurityTest extends WebTestBase {

  void testBindingSecurity() {
    webtest("testBindingSecurity") {

        goToPage '/bindMe'
        verifyText 'Binding security tests'

        goToPage '/bindMe?facet.alwaysBound=funk2live'
        verifyText 'alwaysBound=funk2live'
        verifyText 'neverBound=shouldnotchange'
        verifyText 'other.foo=bar'

        goToPage '/bindMe?facet.alwaysBound=funk2live&facet.neverBound=punkisnotdead'
        verifyText 'alwaysBound=funk2live'
        verifyText 'neverBound=shouldnotchange'
        not {
            verifyText 'neverBound=punkisnotdead'
        }
        verifyText 'other.foo=bar'

        goToPage '/bindMe?other.foo=bazzz'
        verifyText 'other.foo=bar'
        not {
            verifyText 'other.foo=bazzz'
        }

    }
  }


}

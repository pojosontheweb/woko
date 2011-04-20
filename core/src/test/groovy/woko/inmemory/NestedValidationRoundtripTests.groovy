package woko.inmemory

import net.sourceforge.stripes.mock.MockRoundtrip
import woko.actions.WokoActionBean
import woko.actions.TestActionBean

class NestedValidationRoundtripTests extends InMemRoundtripTestBase {

  // make sure Stripes validation works as expected (not really useful but shows how the test works)
  void testBeanValidation() {
    def c = createMockServletContext('wdevel')
    MockRoundtrip trip = new MockRoundtrip(c, '/testValidate.action')
    trip.execute()
    def ab = trip.getActionBean(TestActionBean.class)
    def errors = ab.context.validationErrors
    assert errors.size() == 1
    assert errors.keySet().iterator().next() == "myProp"
  }

  void testFacetValidation() {
    WokoActionBean ab = trip('wdevel', 'testMeToo', null, null)
    // assert prop has not been bound
    assert ab.facet.myProp == null
    // assert validation error has been added
    def errors = ab.context.validationErrors
    assertEquals('unexpected number of errors', 1, errors.size())
    assertEquals('Unexpected key for error', 'facet.myProp', errors.keySet().iterator().next())
  }

}

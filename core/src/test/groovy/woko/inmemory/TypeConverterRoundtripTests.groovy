package woko.inmemory

import net.sourceforge.stripes.mock.MockRoundtrip
import net.sourceforge.stripes.action.ActionBean
import facets.TestWithEntityBinding

class TypeConverterRoundtripTests extends InMemRoundtripTestBase {

  void testWokoTypeConverter() {
    def c = createMockServletContext('wdevel')
    MockRoundtrip trip = new MockRoundtrip(c, '/testMe')
    trip.addParameter('book','1')
    trip.execute()
    def ab = trip.getActionBean(TestWithEntityBinding.class)
    assert ab.book.name == 'Moby Dick'
  }



}

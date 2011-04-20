package woko.inmemory

import net.sourceforge.stripes.mock.MockRoundtrip
import woko.actions.WokoActionBean

class TypeConverterRoundtripTests extends InMemRoundtripTestBase {

  void testWokoTypeConverter() {
    def c = createMockServletContext('wdevel')
    MockRoundtrip trip = new MockRoundtrip(c, '/testMe')
    trip.addParameter('facet.book','1')
    trip.execute()
    WokoActionBean ab = trip.getActionBean(WokoActionBean.class)
    def f = ab.facet
    assert f.book.name == 'Moby Dick'
  }



}

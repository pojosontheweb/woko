package woko.util

import org.junit.Test

class UtilTest {

  private void assertProps(o) {
    def pNames = Util.getPropertyNames(o, ['p2'])
    println pNames
    ['p1', 'p3', 'p4'].each {
      assert pNames.contains(it)
    }
    assert !pNames.contains('p2')
  }

  @Test
  void testGetPropertyNamesOnObject() {
    assertProps(new DummyPojo())
  }


}

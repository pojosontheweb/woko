package woko2.util

import junit.framework.TestCase

class UtilTest extends TestCase {

  private void assertProps(o) {
    def pNames = Util.getPropertyNames(o, ['p2'])
    println pNames
    ['p1', 'p3', 'p4'].each {
      assert pNames.contains(it)
    }
    assert !pNames.contains('p2')
  }

  void testGetPropertyNamesOnObject() {
    assertProps(new DummyPojo())
  }


}

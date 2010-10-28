package woko2.util

import junit.framework.TestCase

class UtilTest extends TestCase {

  private void assertProps(o) {
    def pNames = Util.getPropertyNames(o)
    println pNames
    ['p1', 'p2', 'p3', 'p4'].each {
      assert pNames.contains(it)
    }
  }

  void testGetPropertyNamesOnMap() {
    assertProps([p1:'bar', p2:1, p3:true, p4:[sub:"map"]])
  }

  void testGetPropertyNamesOnObject() {
    assertProps(new DummyPojo())
  }


}

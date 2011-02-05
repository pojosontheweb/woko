package woko2.compass

import junit.framework.TestCase
import org.compass.core.Compass
import woko2.compass.entities.MySearchableEntity
import org.compass.core.CompassSession
import org.compass.core.CompassTransaction
import org.compass.core.CompassHits

class WokoCompassBuilderTest extends TestCase {

  def doInSession(Compass compass, Closure closure) {
    CompassSession s = compass.openSession()
    CompassTransaction tx = s.beginTransaction()
    try {
      closure.call(s)
      tx.commit()
    } catch(Exception e) {
      tx.rollback()
    } finally {
      s.close()
    }
  }

  void testIndexAndSearch() {
    Compass c = new WokoCompassBuilder(['woko2.compass.entities']).buildCompass()
    assert c != null
    MySearchableEntity e = new MySearchableEntity([id:1, prop1:'foo',prop2:'bar'])

    doInSession(c) { CompassSession s->
      s.save(e)
    }

    doInSession(c) { CompassSession s->
      CompassHits hits = s.queryBuilder().queryString('foo').toQuery().hits();
      assert hits.length == 1
      def hit0 = hits.hit(0)
      assert hit0.id == 1
    }

  }
}

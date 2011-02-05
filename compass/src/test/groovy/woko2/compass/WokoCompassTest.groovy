package woko2.compass

import junit.framework.TestCase
import woko2.compass.entities.MySearchableEntity
import org.compass.core.CompassSession
import org.compass.core.CompassHits

class WokoCompassTest extends TestCase {

  void testIndexAndSearch() {
    def wc = new WokoCompass(['woko2.compass.entities'])

    wc.doInSession { CompassSession s->
      s.save(new MySearchableEntity([id:1, prop1:'foo',prop2:'bar']))
    }

    wc.doInSession { CompassSession s->
      CompassHits hits = s.queryBuilder().queryString('foo').toQuery().hits();
      assert hits.length == 1
      def hit0 = hits.hit(0)
      assert hit0.id == 1
    }

  }
}

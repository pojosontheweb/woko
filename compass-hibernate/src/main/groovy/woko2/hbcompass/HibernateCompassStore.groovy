package woko2.hbcompass

import woko2.hibernate.HibernateStore
import woko2.persistence.ResultIterator
import org.compass.core.CompassHits
import org.compass.core.CompassSession
import org.compass.gps.device.hibernate.embedded.HibernateHelper
import org.compass.core.Compass

class HibernateCompassStore extends HibernateStore {

  public static final String DEFAULT_HIBERNATE_CFG_XML = "/woko_default_hibernate_compass.cfg.xml"

  HibernateCompassStore(List<String> packageNames) {
    super(packageNames)
  }

  @Override
  protected String getDefaultHibernateCfgXml() {
    return DEFAULT_HIBERNATE_CFG_XML
  }


  @Override
  ResultIterator search(Object query, Integer start, Integer limit) {
    if (!query instanceof String) {
      throw new IllegalArgumentException("Query must be a String but was " + query.getClass())
    }
    start = start==null ? 0 : start
    limit = limit==null ? -1 : limit
    Compass compass = HibernateHelper.getCompass(sessionFactory);
    CompassSession session = compass.openSession();
    CompassHits hits = session.find((String)query)
    return new CompassResultIterator(hits, start, limit)
  }

}

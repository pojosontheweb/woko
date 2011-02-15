package woko.hbcompass

import woko.hibernate.HibernateStore
import woko.persistence.ResultIterator
import org.compass.core.CompassHits
import org.compass.core.CompassSession
import org.compass.gps.device.hibernate.embedded.HibernateHelper
import org.compass.core.Compass
import org.compass.core.CompassTransaction
import org.compass.core.CompassHitsOperations

class HibernateCompassStore extends HibernateStore {

  public static final String DEFAULT_HIBERNATE_CFG_XML = "/woko_default_hibernate_compass.cfg.xml"

  HibernateCompassStore(List<String> packageNames) {
    super(packageNames)
  }

  @Override
  protected String getDefaultHibernateCfgXml() {
    return DEFAULT_HIBERNATE_CFG_XML
  }

  Compass getCompass() {
    return HibernateHelper.getCompass(sessionFactory)
  }

  @Override
  ResultIterator search(Object query, Integer start, Integer limit) {
    if (!query instanceof String) {
      throw new IllegalArgumentException("Query must be a String but was " + query.getClass())
    }
    start = start==null ? 0 : start
    limit = limit==null ? -1 : limit
    CompassSession session = compass.openSession()
    CompassTransaction tx = session.beginTransaction()
    try {
      CompassHits hits = session.find((String)query)
      int len = hits.length()
      int size = limit == -1 ? len - start : limit
      CompassHitsOperations hitsOps = hits.detach(start, size)
      tx.commit()
      return new CompassResultIterator(hitsOps, start, limit, len)
    } catch(Exception e) {
      tx.rollback()
      throw e
    }
  }

}

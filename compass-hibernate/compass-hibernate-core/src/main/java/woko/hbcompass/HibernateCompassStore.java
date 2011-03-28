package woko.hbcompass;

import org.compass.core.*;
import org.compass.gps.device.hibernate.embedded.HibernateHelper;
import woko.hibernate.HibernateStore;
import woko.persistence.ResultIterator;

import java.util.List;

public class HibernateCompassStore extends HibernateStore {

  public static final String DEFAULT_HIBERNATE_CFG_XML = "/woko_default_hibernate_compass.cfg.xml";

  public HibernateCompassStore(List<String> packageNames) {
    super(packageNames);
  }

  @Override
  protected String getDefaultHibernateCfgXml() {
    return DEFAULT_HIBERNATE_CFG_XML;
  }

  public Compass getCompass() {
    return HibernateHelper.getCompass(getSessionFactory());
  }

  @Override
  public ResultIterator search(Object query, Integer start, Integer limit) {
    if (!(query instanceof String)) {
      throw new IllegalArgumentException("Query must be a String but was " + query.getClass());
    }
    start = start==null ? 0 : start;
    limit = limit==null ? -1 : limit;
    CompassSession session = getCompass().openSession();
    CompassTransaction tx = session.beginTransaction();
    try {
      CompassHits hits = session.find((String)query);
      int len = hits.length();
      int size = limit == -1 ? len - start : limit;
      CompassHitsOperations hitsOps = hits.detach(start, size);
      tx.commit();
      return new CompassResultIterator(hitsOps, start, limit, len);
    } catch(Exception e) {
      tx.rollback();
      throw new RuntimeException(e);
    }
  }

  @Override
  public void close() {
    getCompass().close();
    super.close();
  }
}

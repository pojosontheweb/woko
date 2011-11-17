package woko.hbcompass;

import org.compass.core.*;
import org.compass.gps.device.hibernate.embedded.HibernateHelper;
import woko.hibernate.HibernateStore;
import woko.persistence.ResultIterator;
import woko.util.WLogger;

import java.util.List;

public class HibernateCompassStore extends HibernateStore {

  public static final String DEFAULT_HIBERNATE_CFG_XML = "/woko_default_hibernate_compass.cfg.xml";

  private static final WLogger logger = WLogger.getLogger(HibernateCompassStore.class);

  public HibernateCompassStore(List<String> packageNames) {
    super(packageNames);
  }

  @Override
  protected String getDefaultHibernateCfgXml() {
    return DEFAULT_HIBERNATE_CFG_XML;
  }

  public Compass getCompass() {
    Compass compass = HibernateHelper.getCompass(getSession());
    logger.debug("Returning compass " + compass);
    return compass;
  }

  @Override
  public ResultIterator search(final Object query, Integer start, Integer limit) {
    logger.debug("Executing query : " + query + ", start=" + start + ", limit=" + limit);
    if (!(query instanceof String)) {
      throw new IllegalArgumentException("Query must be a String but was " + query.getClass());
    }
    final int iStart = start==null ? 0 : start;
    final int iLimit = limit==null ? -1 : limit;

    Compass compass = getCompass();
    logger.debug("Querying in template (using compass " + compass + ")");
    return new CompassTemplate(compass).execute(new CompassCallback<CompassResultIterator<?>>() {
      @Override
      public CompassResultIterator<?> doInCompass(CompassSession session) throws CompassException {
        CompassHits hits = session.find((String)query);
        int len = hits.length();
        logger.debug("Query executed, returned " + len + " hit(s)");
        int size = iLimit == -1 ? len - iStart : iLimit;
        CompassHitsOperations hitsOps = hits.detach(iStart, size);
        logger.debug("Hits detached, commiting and returning result iterator");
        return new CompassResultIterator<Object>(hitsOps, iStart, iLimit, len);
      }
    });
  }

  @Override
  public void close() {
    logger.debug("Closing compass...");
    getCompass().close();
    logger.debug("... compass closed");
    super.close();
  }
}

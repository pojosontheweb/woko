/*
 * Copyright 2001-2012 Remi Vankeisbelck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package woko.hbcompass;

import org.compass.core.*;
import org.compass.gps.device.hibernate.embedded.HibernateHelper;
import woko.hibernate.HibernateStore;
import woko.persistence.ResultIterator;
import woko.util.WLogger;

import java.util.List;

/**
 * HibernateStore extension that uses Compass search engine for index/search operations.
 */
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

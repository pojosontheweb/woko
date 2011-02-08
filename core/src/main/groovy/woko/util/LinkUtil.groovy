package woko.util

import woko.Woko
import woko.persistence.ObjectStore

class LinkUtil {

  static String getUrl(Woko woko, Object o, String facetName) {
    ObjectStore s = woko.getObjectStore()
    String className = s.getClassMapping(o.getClass());
    String key = s.getKey(o)
    facetName = facetName==null ? 'view' : facetName
    return "$facetName/$className/$key"
  }

}

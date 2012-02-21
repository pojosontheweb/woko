package woko.util;

import woko.Woko;
import woko.facets.builtin.WokoFacets;
import woko.persistence.ObjectStore;

public class LinkUtil {

  public static String getUrl(Woko woko, Object o, String facetName) {
    ObjectStore s = woko.getObjectStore();
    String className = s.getClassMapping(o.getClass());
    String key = s.getKey(o);
    facetName = facetName==null ? WokoFacets.view : facetName;
    return facetName + "/" + className + "/" + key;
  }

}

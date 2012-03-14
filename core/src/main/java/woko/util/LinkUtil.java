/*
 * Copyright 2001-2010 Remi Vankeisbelck
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

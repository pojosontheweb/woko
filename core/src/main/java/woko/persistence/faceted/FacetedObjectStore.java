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

package woko.persistence.faceted;

import woko.Woko;
import woko.actions.WokoRequestInterceptor;
import woko.persistence.ObjectStore;
import woko.persistence.ResultIterator;
import woko.util.WLogger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public abstract class FacetedObjectStore implements ObjectStore {

  private static final WLogger logger = WLogger.getLogger(FacetedObjectStore.class);

  private HttpServletRequest getRequest() {
    return WokoRequestInterceptor.getRequest();
  }

  private Woko<?,?,?,?> getWoko() {
    HttpServletRequest request = getRequest();
    if (request==null) {
      throw new IllegalStateException("Unable to get thread-bound request from WokoRequestInterceptor. Interceptor has not been invoked ?");
    }
    return Woko.getWoko(request.getSession().getServletContext());
  }

  @Override
  public Object load(String className, String key) {
    if (className==null && key==null) {
      logger.debug("className and key are null, nothing done");
      return null;
    }

    HttpServletRequest request = getRequest();
    Woko<?,?,?,?> woko = getWoko();

    logger.debug("Trying to load object for class " + className + ", key " + key);
    Class<?> mappedClass = getMappedClass(className);
    if (mappedClass==null) {
      logger.warn("No mapped class for className " + className + ", returning new Object()");
      return null;
    }
    if (key==null) {
      logger.debug("Key not specified for className " + className + ", creating transient instance");
      // create transient instance
      StoreNew storeNew = woko.getFacet("storeNew", request, null, mappedClass, true);
      return storeNew.newInstance(this, mappedClass);
    }

    // load object
    StoreLoad storeLoad = woko.getFacet("storeLoad", request, null, mappedClass, true);
    return storeLoad.load(this, mappedClass, key);
  }

  private <T> T getFacet(Class<T> facetClass, String name, Object targetObject, Class<?> targetObjectClass) {
    HttpServletRequest request = getRequest();
    Woko<?,?,?,?> woko = getWoko();
    return woko.getFacet(name, request, targetObject, targetObjectClass, true);
  }

  @Override
  public Object save(Object obj) {
    return getFacet(StoreSave.class, "storeSave", obj, obj.getClass()).save(this, obj);
  }

  @Override
  public Object delete(Object obj) {
    return getFacet(StoreDelete.class, "storeDelete", obj, obj.getClass()).delete(this, obj);
  }

  @Override
  public String getKey(Object obj) {
    return getFacet(StoreGetKey.class, "storeGetKey", obj, obj.getClass()).getKey(this, obj);
  }

  @Override
  public String getClassMapping(Class<?> clazz) {
    return clazz.getSimpleName();
  }

  @Override
  public Class<?> getMappedClass(String className) {
    if (className==null) {
      return null;
    }
    try {
      return Class.forName(className);
    } catch(ClassNotFoundException cnfex) {
      // try simple name
      for (Class<?> clazz : getMappedClasses()) {
        String simpleName = clazz.getSimpleName();
        if (simpleName.equals(className)) {
          return clazz;
        }
      }
    }
    return null;
  }

  @Override
  public ResultIterator list(String className, Integer start, Integer limit) {
    Class<?> clazz = getMappedClass(className);
    return getFacet(StoreList.class, "storeList", null, clazz).list(this, className, start, limit);
  }

  @Override
  public abstract List<Class<?>> getMappedClasses();

  @Override
  public ResultIterator search(Object query, Integer start, Integer limit) {
    return getFacet(StoreSearch.class, "storeSearch", query, query.getClass()).search(this, query, start, limit);
  }
}

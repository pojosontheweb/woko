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

package woko.persistence;

import java.util.List;

/**
 * Object Store contract. The store provides access to CRUD operations on
 * Woko-managed POJOs.
 */
public interface ObjectStore {

    /**
     * Load an object from the store using passed parameters
     * @param className the (mapped) class name of the object to load
     * @param key the key (ID) of the object to load
     * @return the loaded object if any, <code>null</code> otherwise
     */
    Object load(String className, String key);

    /**
     * Save or update passed object to the store
     * @param obj a Woko-managed POJO
     * @return the saved object
     */
    Object save(Object obj);

    /**
     * Delete passed object permanently from the store
     * @param obj a Woko-managed POJO to delete
     * @return
     */
    Object delete(Object obj);

    /**
     * Return the key (ID) for passed object
     * @param obj a Woko-managed POJO
     * @return the key for passed object
     */
    String getKey(Object obj);

    /**
     * Return the class mapping for passed class
     * @param clazz the Java Class to get the mapping for
     * @return the mapping for passed class if any
     */
    String getClassMapping(Class<?> clazz);

    /**
     * Return the class for passed instance.
     * This method can return o.getClass(), but in some
     * cases like when the object is proxified, you might do additional work like deproxify etc.
     * Woko uses this method instead of o.getClass() in order to make sure
     * it works with the actual class instead of a proxy, and avoid any proxy-related issues.
     * @param o the object to get class mapping for
     * @return the mapping for passed class if any
     */
    Class<?> getObjectClass(Object o);

    /**
     * Return the Java Class for passed mapped class name
     * @param className the mapped class name
     * @return the Java class for passed mapped class name
     */
    Class<?> getMappedClass(String className);

    /**
     * List instances of the passed mapped class, with pagination enabled
     * @param className the mapped class name
     * @param start the start offset
     * @param limit the limit
     * @return a <code>ResultIterator</code> of the listed objects
     */
    ResultIterator<?> list(String className, Integer start, Integer limit);

    /**
     * Return a list of all Woko-managed (mapped) classes
     * @return a list of all Woko-managed (mapped) classes
     */
    List<Class<?>> getMappedClasses();

    /**
     * Search instances in a full-text fashion, with pagination enabled
     * @param query the full text query
     * @param start the start offset
     * @param limit the limit
     * @return a <code>ResultIterator</code> of the matching objects
     */
    ResultIterator<?> search(Object query, Integer start, Integer limit);

    /**
     * Close the store and release all resources (database connection, etc.).
     * Invoked when the app shuts down.
     */
    void close();

}
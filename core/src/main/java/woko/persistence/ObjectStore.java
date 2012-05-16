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

public interface ObjectStore {

  Object load(String className, String key);

  Object save(Object obj);

  Object delete(Object obj);

  String getKey(Object obj);

  String getClassMapping(Class<?> clazz);

  Class<?> getMappedClass(String className);

  ResultIterator<?> list(String className, Integer start, Integer limit);

  List<Class<?>> getMappedClasses();

  ResultIterator<?> search(Object query, Integer start, Integer limit);

  void close();

}
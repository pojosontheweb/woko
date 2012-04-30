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

package woko.facets;

public class FacetNotFoundException extends RuntimeException {

  public FacetNotFoundException(String facetName, String className, String key, String username) {
    super("Facet not found, facetName=" + facetName +
            ", className=" + className +
            ", key=" + key +
            ", username=" + username);
  }

  public FacetNotFoundException(String facetName, Object targetObject, Class targetObjectClass, String username) {
    super("Facet not found, facetName=" + facetName +
            ", targetObject=" + targetObject +
            ", targetObjectClass=" + targetObjectClass +
            ", username=" + username);
  }

}

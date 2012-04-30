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

import javax.servlet.http.HttpServletRequest;

public abstract class BaseFragmentFacet extends BaseFacet implements FragmentFacet {

  private HttpServletRequest request;

  public String getFragmentPath(HttpServletRequest request) {
    this.request = request;
    return this.getPath();
  }

  public abstract String getPath();

  public HttpServletRequest getRequest() {
    return request;
  }

}

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

package woko.actions;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;

import javax.servlet.http.HttpServletRequest;

/**
 * Interceptor that makes the http request available everywhere in the app via static
 * method.
 * Uses a <code>ThreadLocal</code> in order to store the request and make it available.
 */
@Intercepts({LifecycleStage.RequestInit, LifecycleStage.RequestComplete})
public class WokoRequestInterceptor implements net.sourceforge.stripes.controller.Interceptor {

  private static ThreadLocal<HttpServletRequest> requests = new ThreadLocal<HttpServletRequest>();

  public Resolution intercept(ExecutionContext context) throws Exception {
    LifecycleStage stage = context.getLifecycleStage();
    HttpServletRequest request = context.getActionBeanContext().getRequest();
    if (stage == LifecycleStage.RequestInit) {
      requests.set(request);
    } else if (stage.equals(LifecycleStage.RequestComplete)) {
      requests.remove();
    }
    return context.proceed();
  }

  public static HttpServletRequest getRequest() {
    return requests.get();
  }


}

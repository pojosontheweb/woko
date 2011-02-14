package woko.actions

import net.sourceforge.stripes.controller.LifecycleStage
import net.sourceforge.stripes.controller.Intercepts
import javax.servlet.http.HttpServletRequest
import net.sourceforge.stripes.controller.ExecutionContext
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.controller.Interceptor

@Intercepts([LifecycleStage.RequestInit, LifecycleStage.RequestComplete])
class WokoRequestInterceptor implements Interceptor {

  private static ThreadLocal<HttpServletRequest> requests = new ThreadLocal<HttpServletRequest>()

  Resolution intercept(ExecutionContext context) throws Exception {
    LifecycleStage stage = context.lifecycleStage
    HttpServletRequest request = context.actionBeanContext.request
    if (stage == LifecycleStage.RequestInit) {
      requests.set(request)
    } else if (stage.equals(LifecycleStage.RequestComplete)) {
      requests.remove()
    }
    try {
      return context.proceed();
    } catch (Exception e) {
      throw e
    }
  }

  static HttpServletRequest getRequest() {
    return requests.get()
  }


}

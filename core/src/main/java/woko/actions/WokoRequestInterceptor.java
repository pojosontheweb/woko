package woko.actions;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.LifecycleStage;

import javax.servlet.http.HttpServletRequest;

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

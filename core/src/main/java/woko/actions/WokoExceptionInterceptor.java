package woko.actions;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;

import javax.servlet.http.HttpServletRequest;

@Intercepts({LifecycleStage.EventHandling, LifecycleStage.ResolutionExecution})
public class WokoExceptionInterceptor implements Interceptor {

    public static final String WOKO_REQUEST_EXCEPTION = "wokoException";

    @Override
    public Resolution intercept(ExecutionContext context) throws Exception {
        try {
            return context.proceed();
        } catch(Exception e) {
            context.getActionBeanContext().getRequest().setAttribute(WOKO_REQUEST_EXCEPTION, e);
            throw e;
        }
    }

    public static Exception getException(HttpServletRequest request) {
        return (Exception)request.getAttribute(WOKO_REQUEST_EXCEPTION);
    }
}

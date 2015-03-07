package woko.actions.auth.rememberme;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import woko.Woko;
import woko.actions.WokoActionBean;
import woko.facets.ResolutionFacet;
import woko.facets.builtin.Logout;

@Intercepts({LifecycleStage.BindingAndValidation})
public class RememberMeCleanerInterceptor implements Interceptor {

    @Override
    public Resolution intercept(ExecutionContext context) throws Exception {
        ActionBean ab = context.getActionBean();
        if (ab instanceof WokoActionBean) {
            WokoActionBean<?,?,?,?> wab = (WokoActionBean<?,?,?,?>)ab;
            ResolutionFacet f = wab.getFacet();
            Woko<?,?,?,?> woko = wab.getContext().getWoko();
            if (f instanceof Logout) {
                String username = woko.getUsername(context.getActionBeanContext().getRequest());
                if (username!=null) {
                    // purge all long term cookies for the user !
                    RmCookieStore cookieStore = woko.getIoc().getComponent(RmCookieStore.KEY);
                    if (cookieStore!=null) {
                        Resolution res = context.proceed();
                        cookieStore.deleteAllForUser(username);
                        return res;
                    }
                }
            }
        }
        return context.proceed();
    }
}

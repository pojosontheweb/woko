package woko.actions.auth.rememberme;

import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.config.ConfigurableComponent;
import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import woko.Woko;
import woko.actions.auth.builtin.WokoLogin;
import woko.util.WLogger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Intercepts({LifecycleStage.RequestInit})
public class RememberMeInterceptor implements Interceptor, ConfigurableComponent {

    public static final String COOKIE_NAME = "RememberMe";

    private static final WLogger logger = WLogger.getLogger(RememberMeInterceptor.class);

    public static final String COOKIE_VAL_SEPARATOR = "~-/:/-~";

    private Woko<?,?,?,?> woko;
    private RmCookieStore cookieStore;

    @Override
    public void init(Configuration configuration) throws Exception {
        woko = Woko.getWoko(configuration.getServletContext());
        cookieStore = woko.getIoc().getComponent(RmCookieStore.KEY);
    }

    @Override
    public Resolution intercept(ExecutionContext context) throws Exception {
        if (cookieStore!=null) {
            ActionBeanContext abc = context.getActionBeanContext();
            HttpServletRequest request = abc.getRequest();
            String username = woko.getUsername(request);
            if (username == null) {

                Cookie[] cookies = request.getCookies();
                if (cookies==null) {
                    return null;
                }
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(COOKIE_NAME)) {
                        String val = cookie.getValue();
                        String[] parts = val.split(COOKIE_VAL_SEPARATOR);
                        if (parts.length!=3) {
                            logger.warn("Received cookie for " + COOKIE_NAME + " with invalid part count : " + val);
                            return null;
                        }
                        String cUsername = parts[0];
                        String cSeries = parts[1];
                        String cToken = parts[2];

                        RmCookie rmCookie = cookieStore.getCookie(cUsername, cSeries);
                        if (rmCookie!=null) {

                            String rmToken = rmCookie.getToken();
                            if (rmToken.equals(cToken)) {

                                // cookie matches stored info : user is authenticated !
                                // we update the token and generate a new cookie
                                RmCookie newRmCookie = cookieStore.updateToken(rmCookie);
                                String newVal = newRmCookie.toPath();
                                Cookie newCookie = new Cookie(COOKIE_NAME, newVal);
                                abc.getResponse().addCookie(newCookie);

                                // we mark the user's session as authenticated, so that
                                // the SessionUsernameResolutionStrategy works transparently
                                request.getSession().setAttribute(WokoLogin.SESSION_ATTR_CURRENT_USER, cUsername);

                            } else {
                                // token doesn't match !! theft ?
                                cookieStore.deleteAllForUser(cUsername);
                                // TODO find a way to notify ?
                            }
                        }

                    }
                }

            }
        }
        return context.proceed();
    }

}

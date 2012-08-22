package woko.actions.auth.builtin;

import net.sourceforge.stripes.action.OnwardResolution;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginHttpsRedirectResolution extends OnwardResolution<LoginHttpsRedirectResolution> {

    private final String serverName;
    private final int portHttps;

    public LoginHttpsRedirectResolution(String serverName, int portHttps) {
        super(WokoLogin.class);
        this.serverName = serverName;
        this.portHttps = portHttps;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        StringBuilder sb = new StringBuilder().
          append("https://").
          append(serverName);
        if (portHttps!=443) {
            sb.append(":").append(Integer.toString(portHttps));
        }
        String contextPath = request.getContextPath();
        if (contextPath.length()>1) {
            sb.append(contextPath);
        }
        sb.append(getUrl(request.getLocale()));
        response.sendRedirect(response.encodeRedirectURL(sb.toString()));
    }
}

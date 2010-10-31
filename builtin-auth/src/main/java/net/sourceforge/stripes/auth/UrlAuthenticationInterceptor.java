package net.sourceforge.stripes.auth;

import net.sourceforge.stripes.controller.ExecutionContext;

public class UrlAuthenticationInterceptor extends AuthenticationInterceptor {    

    @Override
    protected boolean requiresAuthentication(ExecutionContext executionContext) {
        return false;        
    }
}

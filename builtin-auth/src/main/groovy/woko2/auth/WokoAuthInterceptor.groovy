package woko2.auth

import net.sourceforge.stripes.controller.ExecutionContext
import net.sourceforge.stripes.config.Configuration

class WokoAuthInterceptor { //extends AuthenticationInterceptor implements ConfigurableComponent {

  public static final

  void init(Configuration configuration) {
    configuration.getBootstrapPropertyResolver().getProperty("")
  }



  protected boolean requiresAuthentication(ExecutionContext executionContext) {

  }


}

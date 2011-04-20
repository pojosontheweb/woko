package woko.ri;

import woko.auth.builtin.SessionUsernameResolutionStrategy;
import woko.hbcompass.HibernateCompassInMemWokoInitListener;
import woko.users.UsernameResolutionStrategy;

public class RiWokoInitListener extends HibernateCompassInMemWokoInitListener {

  @Override
  protected UsernameResolutionStrategy createUsernameResolutionStrategy() {
    return new SessionUsernameResolutionStrategy();
  }

}

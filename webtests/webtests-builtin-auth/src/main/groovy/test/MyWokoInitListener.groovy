package test

import woko.hbcompass.HibernateCompassInMemWokoInitListener
import woko.Woko
import woko.auth.builtin.SessionUsernameResolutionStrategy

class MyWokoInitListener extends HibernateCompassInMemWokoInitListener {

  @Override
  Woko createWoko() {
    return new Woko(
            createObjectStore(),
            createUserManager(),
            createFallbackRoles(),
            Woko.createDefaultFacetDescriptorManager(),
            new SessionUsernameResolutionStrategy())

  }


}

package test

import woko2.hbcompass.HibernateCompassInMemWokoInitListener
import woko2.Woko
import woko2.auth.builtin.SessionUsernameResolutionStrategy

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

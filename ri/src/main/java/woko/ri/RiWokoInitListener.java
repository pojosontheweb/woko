package woko.ri;

import woko.Woko;
import woko.auth.builtin.SessionUsernameResolutionStrategy;
import woko.hbcompass.HibernateCompassInMemWokoInitListener;

public class RiWokoInitListener extends HibernateCompassInMemWokoInitListener {

    @Override
    public Woko createWoko() {
        return new Woko(
            createObjectStore(),
            createUserManager(),
            createFallbackRoles(),
            Woko.createDefaultFacetDescriptorManager(),
            new SessionUsernameResolutionStrategy());
    }
}

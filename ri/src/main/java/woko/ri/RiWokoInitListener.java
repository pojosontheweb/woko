package woko.ri;

import woko.auth.builtin.SessionUsernameResolutionStrategy;
import woko.ext.usermanagement.hibernate.HibernateUserManager;
import woko.hbcompass.HibernateCompassWokoInitListener;
import woko.hibernate.HibernateStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

public class RiWokoInitListener extends HibernateCompassWokoInitListener {

    @Override
    protected UsernameResolutionStrategy createUsernameResolutionStrategy() {
        return new SessionUsernameResolutionStrategy();
    }

    @Override
    protected UserManager createUserManager() {
        return new HibernateUserManager((HibernateStore)getObjectStore()).createDefaultUsers();
    }
}

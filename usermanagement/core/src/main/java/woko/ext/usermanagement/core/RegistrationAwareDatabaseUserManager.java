package woko.ext.usermanagement.core;

public abstract class RegistrationAwareDatabaseUserManager<T extends RegistrationAwareDatabaseUserManager<T,U>, U extends User>
        extends DatabaseUserManager<T,U>
        implements RegistrationAwareUserManager<U> {

    protected RegistrationAwareDatabaseUserManager(Class<U> userClass) {
        super(userClass);
    }



}

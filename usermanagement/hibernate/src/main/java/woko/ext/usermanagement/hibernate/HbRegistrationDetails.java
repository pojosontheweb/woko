package woko.ext.usermanagement.hibernate;

import woko.ext.usermanagement.core.RegistrationDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class HbRegistrationDetails<U extends HbUser> implements RegistrationDetails<U> {

    @Id
    private String key;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private HbUser user;

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    @SuppressWarnings("unchecked")
    public U getUser() {
        return (U)user;
    }

    public void setUser(U user) {
        this.user = user;
    }
}

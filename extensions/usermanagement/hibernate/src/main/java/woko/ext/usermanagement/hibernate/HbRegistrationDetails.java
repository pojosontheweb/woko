package woko.ext.usermanagement.hibernate;

import woko.ext.usermanagement.core.RegistrationDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class HbRegistrationDetails<U extends HbUser> implements RegistrationDetails<U> {

    @Id
    @Column(name = "registration_key")
    private String key;

    @ManyToOne(fetch = FetchType.LAZY)
    private HbUser user;

    @NotNull
    private String secretToken;

    private Date createdOn = new Date();

    private Date activatedOn;

    private Long activatedUserId;

    private String activatedUsername;

    private String activatedEmail;

    @Override
    public boolean activate() {
        if (user!=null) {
            activatedOn = new Date();
            activatedUserId = user.getId();
            activatedUsername = user.getUsername();
            activatedEmail = user.getEmail();
            user = null;
            return true;
        }
        return false;
    }

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSecretToken() {
        return secretToken;
    }

    public void setSecretToken(String secretToken) {
        this.secretToken = secretToken;
    }

    @Override
    @SuppressWarnings("unchecked")
    public U getUser() {
        return (U)user;
    }

    public void setUser(U user) {
        this.user = user;
    }

    @Override
    public Date getCreatedOn() {
        return createdOn;
    }

    @Override
    public Date getActivatedOn() {
        return activatedOn;
    }

    @Override
    public Long getActivatedUserId() {
        return activatedUserId;
    }

    @Override
    public String getActivatedUsername() {
        return activatedUsername;
    }

    @Override
    public String getActivatedEmail() {
        return activatedEmail;
    }

    @Override
    public String toString() {
        return "HbRegistrationDetails{" +
                "key='" + key + '\'' +
                ", user=" + user +
                ", secretToken='" + secretToken + '\'' +
                ", createdOn=" + createdOn +
                ", activatedOn=" + activatedOn +
                ", activatedUserId=" + activatedUserId +
                ", activatedUsername='" + activatedUsername + '\'' +
                ", activatedEmail='" + activatedEmail + '\'' +
                '}';
    }
}

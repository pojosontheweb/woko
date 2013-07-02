package woko.ext.usermanagement.hibernate;

import woko.ext.usermanagement.core.ResetPasswordDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class HbResetPasswordDetails implements ResetPasswordDetails {

    @Id
    @Column(name = "registration_key")
    private String key;

    @NotNull
    private String email;

    @NotNull
    private Date creationDate;

    private Date resetDate;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getResetDate() {
        return resetDate;
    }

    public void setResetDate(Date resetDate) {
        this.resetDate = resetDate;
    }


}

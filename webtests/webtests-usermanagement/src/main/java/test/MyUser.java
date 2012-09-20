package test;

import woko.ext.usermanagement.hibernate.HbUser;

import javax.persistence.Entity;

@Entity
public class MyUser extends HbUser {

    private String prop1;

    public String getProp1() {
        return prop1;
    }

    public void setProp1(String prop1) {
        this.prop1 = prop1;
    }
}

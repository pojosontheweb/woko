package test

import woko.ext.usermanagement.hibernate.HbUser
import javax.persistence.Entity

@Entity
class MyUser extends HbUser {

    String prop1

}

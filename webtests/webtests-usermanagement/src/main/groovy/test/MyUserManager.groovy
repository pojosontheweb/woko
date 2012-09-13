package test

import woko.ext.usermanagement.hibernate.HibernateUserManager
import woko.hibernate.HibernateStore
import woko.ext.usermanagement.core.DatabaseUserManager
import woko.hibernate.TxCallback

class MyUserManager extends HibernateUserManager {

    MyUserManager(HibernateStore hibernateStore) {
        super(hibernateStore, MyUser.class)
        defaultRoles = ["usermanager","developer"] // need to put usermanahe
    }

    @Override
    DatabaseUserManager createDefaultUsers() {
        super.createDefaultUsers()
        for (int i=0; i<1000; i++) {
            MyUser u1 = createUser("testuser$i", encodePassword("testuser$i"), ["testuser"])
            u1.prop1 = "foobar$i"
            hibernateStore.doInTx({ store, session ->
                store.save(u1)
            } as TxCallback)
        }
        return this
    }


}

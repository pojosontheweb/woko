package test

import woko.ext.usermanagement.hibernate.HibernateUserManager
import woko.hibernate.HibernateStore
import woko.ext.usermanagement.core.DatabaseUserManager
import woko.hibernate.TxCallback

class MyUserManager extends HibernateUserManager {

    MyUserManager(HibernateStore hibernateStore) {
        super(hibernateStore, MyUser.class)
    }

    @Override
    DatabaseUserManager createDefaultUsers() {
        super.createDefaultUsers()
        MyUser u1 = createUser("testuser", encodePassword("testuser"), ["testuser"])
        u1.prop1 = "foobar"
        hibernateStore.doInTx({ store, session ->
            store.save(u1)
        } as TxCallback)
        return this
    }


}

package test;

import org.hibernate.Session;
import woko.ext.usermanagement.hibernate.HibernateUserManager;
import woko.hibernate.HibernateStore;
import woko.hibernate.TxCallback;

import java.util.Arrays;

public class MyUserManager extends HibernateUserManager<MyUser> {

    public MyUserManager(HibernateStore hibernateStore) {
        super(hibernateStore, MyUser.class);
        setDefaultRoles(Arrays.asList("usermanager","developer")); // usermanager role added
    }

    @Override
    public MyUserManager createDefaultUsers() {
        super.createDefaultUsers();
        for (int i=0; i<1000; i++) {
            final MyUser u1 = createUser("testuser" + i, "testuser" + i, Arrays.asList("testuser"));
            u1.setProp1("foobar" + i);
            getHibernateStore().doInTx(new TxCallback() {
                @Override
                public void execute(HibernateStore store, Session session) throws Exception {
                    store.save(u1);
                }
            });
        }
        return this;
    }



}

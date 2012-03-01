package woko.ext.usermanagement.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import woko.ext.usermanagement.core.DatabaseUserManager;
import woko.ext.usermanagement.core.User;
import woko.hibernate.HibernateStore;
import woko.hibernate.TxCallback;
import woko.hibernate.TxCallbackWithResult;
import woko.persistence.ListResultIterator;
import woko.persistence.ResultIterator;

import java.util.ArrayList;
import java.util.List;

public class HibernateUserManager extends DatabaseUserManager {

    private final HibernateStore hibernateStore;
    private final Class<? extends User> userClass;

    public HibernateUserManager(HibernateStore hibernateStore) {
        this(hibernateStore, HbUser.class);
    }

    public HibernateUserManager(HibernateStore hibernateStore, Class<? extends User> userClass) {
        this.hibernateStore = hibernateStore;
        this.userClass = userClass;
    }

    public Class<? extends User> getUserClass() {
        return userClass;
    }

    public HibernateStore getHibernateStore() {
        return hibernateStore;
    }

    @Override
    public User getUserByUsername(String username) {
        Session s = hibernateStore.getSession();
        List l = s.createCriteria(getUserClass()).add(Restrictions.eq("username", username)).list();
        if (l.size()==0) {
            return null;
        }
        if (l.size()>1) {
            throw new IllegalStateException("more than 1 users with username==" + username);
        }
        return (User)l.get(0);
    }

    @Override
    public ResultIterator<User> listUsers(Integer start, Integer limit) {
        // TODO invoke store "list"
        Session s = hibernateStore.getSession();
        Criteria crit =  s.createCriteria(getUserClass());
        int st = -1;
        if (start!=null && start!=-1) {
            crit.setFirstResult(start);
            st = start;
        }
        int lm = -1;
        if (limit!=null && limit!=-1) {
            crit.setMaxResults(limit);
            lm = limit;
        }
        @SuppressWarnings("unchecked")
        List<User> l = (List<User>)crit.list();

        // second request (count)
        String query = new StringBuilder("select count(*) from ").append(getUserClass().getSimpleName()).toString();
        Long count = (Long)s.createQuery(query).list().get(0);

        return new ListResultIterator<User>(l, st, lm, count.intValue());
    }


    @Override
    protected User createUser(final String username, final String password, final List<String> roles) {
        return getHibernateStore().doInTxWithResult(new TxCallbackWithResult<User>() {
            @Override
            public User execute(HibernateStore store, Session session) throws Exception {
                User u = getUserByUsername(username);
                if (u==null) {
                    User user = HibernateUserManager.this.getUserClass().newInstance();
                    user.setUsername(username);
                    user.setPassword(encodePassword(password));
                    ArrayList<String> rolesCopy = new ArrayList<String>(roles);
                    user.setRoles(rolesCopy);
                    store.save(user);
                    return user;
                }
                return u;
            }
        });
    }
}

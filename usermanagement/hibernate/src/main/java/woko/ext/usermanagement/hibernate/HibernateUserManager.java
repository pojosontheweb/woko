package woko.ext.usermanagement.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import woko.ext.usermanagement.core.DatabaseUserManager;
import woko.ext.usermanagement.core.User;
import woko.hibernate.HibernateStore;

import java.util.ArrayList;
import java.util.List;

public class HibernateUserManager extends DatabaseUserManager {

    private final HibernateStore hibernateStore;

    public HibernateUserManager(HibernateStore hibernateStore) {
        this.hibernateStore = hibernateStore;
    }

    @Override
    protected User getUserByUsername(String username) {
        Session s = hibernateStore.getSession();
        List l = s.createCriteria(HbUser.class).add(Restrictions.eq("username", username)).list();
        if (l.size()==0) {
            return null;
        }
        if (l.size()>1) {
            throw new IllegalStateException("more than 1 users with username==" + username);
        }
        return (User)l.get(0);
    }

    @Override
    protected User createUser(String username, String password, List<String> roles) {
        Session session = hibernateStore.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try {
            User u = getUserByUsername(username);
            if (u==null) {
                HbUser user = new HbUser();
                user.setUsername(username);
                user.setPassword(encodePassword(password));
                ArrayList<String> rolesCopy = new ArrayList<String>(roles);
                user.setRoles(rolesCopy);
                hibernateStore.save(user);
                tx.commit();
                return user;
            }
            tx.commit();
            return null;
        } catch(Exception e) {
            tx.rollback();
            return null;
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }
}

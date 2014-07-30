package woko.actions.auth.rememberme.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import woko.actions.auth.rememberme.RmCookie;
import woko.actions.auth.rememberme.RmCookieStore;
import woko.hibernate.HibernateStore;
import woko.persistence.TransactionCallback;
import woko.persistence.TransactionCallbackWithResult;

import java.util.UUID;

public class HibernateRmCookieStore implements RmCookieStore {

    private final HibernateStore store;

    public HibernateRmCookieStore(HibernateStore store) {
        this.store = store;
    }

    private HibernateRmCookie getHbCookie(final String username, final String series, final String token) {
        Criteria c =  store.getSession().createCriteria(HibernateRmCookie.class)
                .add(Restrictions.eq("username", username))
                .add(Restrictions.eq("series", series));
        if (token!=null) {
            c.add(Restrictions.eq("token", token));
        }
        return (HibernateRmCookie)c.uniqueResult();
    }

    @Override
    public RmCookie getCookie(final String username, final String series) {
        HibernateRmCookie hbc = getHbCookie(username, series, null);
        if (hbc==null) {
            return null;
        }
        return new RmCookie(hbc.getUsername(), hbc.getSeries(), hbc.getToken());
    }

    @Override
    public RmCookie updateToken(final RmCookie cookie) {
        HibernateRmCookie hbc = getHbCookie(cookie.getUsername(), cookie.getSeries(), cookie.getToken());
        if (hbc==null) {
            return null;
        }
        hbc.setToken(UUID.randomUUID().toString());
        store.save(hbc);
        return new RmCookie(hbc.getUsername(), hbc.getSeries(), hbc.getToken());
    }

    @Override
    public void deleteAllForUser(final String username) {
        String hql = "delete from HibernateRmCookie where username = :username";
        store.getSession()
                .createQuery(hql)
                .setString("username", username)
                .executeUpdate();
    }

    @Override
    public RmCookie createCookie(final String user) {
        HibernateRmCookie hbc = new HibernateRmCookie();
        hbc.setUsername(user);
        hbc.setSeries(UUID.randomUUID().toString());
        hbc.setToken(UUID.randomUUID().toString());
        store.save(hbc);
        return new RmCookie(hbc.getUsername(), hbc.getSeries(), hbc.getToken());
    }
}



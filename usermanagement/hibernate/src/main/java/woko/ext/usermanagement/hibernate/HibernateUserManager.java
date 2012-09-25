/*
 * Copyright 2001-2012 Remi Vankeisbelck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package woko.ext.usermanagement.hibernate;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import woko.ext.usermanagement.core.*;
import woko.hibernate.HibernateStore;
import woko.hibernate.TxCallbackWithResult;
import woko.persistence.ListResultIterator;
import woko.persistence.ResultIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class HibernateUserManager<T extends HibernateUserManager<T,U>, U extends HbUser>
        extends DatabaseUserManager<T,U>
        implements RegistrationAwareUserManager<U> {

    private final HibernateStore hibernateStore;

    private AccountStatus registeredAccountStatus = AccountStatus.Registered;
    private List<String> registeredRoles = Collections.emptyList();

    public HibernateUserManager(HibernateStore hibernateStore, Class<U> userClass) {
        super(userClass);
        this.hibernateStore = hibernateStore;
    }


    public HibernateStore getHibernateStore() {
        return hibernateStore;
    }

    @Override
    @SuppressWarnings("unchecked")
    public U getUserByUsername(String username) {
        Session s = hibernateStore.getSession();
        List l = s.createCriteria(getUserClass())
                .add(Restrictions.eq("username", username))
                .setFlushMode(FlushMode.MANUAL)
                .list();
        if (l.size()==0) {
            return null;
        }
        if (l.size()>1) {
            throw new IllegalStateException("more than 1 users with username==" + username);
        }
        return (U)l.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public U getUserByEmail(String email) {
        Session s = hibernateStore.getSession();
        List l = s.createCriteria(getUserClass())
                .add(Restrictions.eq("email", email))
                .setFlushMode(FlushMode.MANUAL)
                .list();
        if (l.size()==0) {
            return null;
        }
        if (l.size()>1) {
            throw new IllegalStateException("more than 1 users with email==" + email);
        }
        return (U)l.get(0);
    }

    @Override
    public ResultIterator<U> listUsers(Integer start, Integer limit) {
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
        List<U> l = (List<U>)crit.list();

        // second request (count)
        String query = new StringBuilder("select count(*) from ").append(getUserClass().getSimpleName()).toString();
        Long count = (Long)s.createQuery(query).list().get(0);

        return new ListResultIterator<U>(l, st, lm, count.intValue());
    }

    @Override
    @Deprecated
    public U createUser(final String username, final String password, final List<String> roles) {
        return createUser(username,password,"unknown@unknown.com",roles, AccountStatus.Active);
    }

    @Override
    public U createUser(final String username, final String password, final String email, final List<String> roles, final AccountStatus accountStatus) {
        TxCallbackWithResult<U> cb = new TxCallbackWithResult<U>() {
            @Override
            public U execute(HibernateStore store, Session session) throws Exception {
                U u = getUserByUsername(username);
                if (u==null) {
                    U user = HibernateUserManager.this.getUserClass().newInstance();
                    user.setUsername(username);
                    user.setPassword(encodePassword(password));
                    user.setAccountStatus(accountStatus);
                    user.setEmail(email);
                    ArrayList<String> rolesCopy = new ArrayList<String>(roles);
                    user.setRoles(rolesCopy);
                    store.save(user);
                    return user;
                }
                return u;
            }
        };
        HibernateStore store = getHibernateStore();
        Transaction tx = store.getSession().getTransaction();
        if (tx.isActive()) {
            try {
                return cb.execute(store, store.getSession());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return getHibernateStore().doInTxWithResult(cb);
        }

    }

    @Override
    public RegistrationDetails<U> createRegistration(U user) {
        HbRegistrationDetails<U> registration = new HbRegistrationDetails<U>();
        registration.setUser(user);
        registration.setKey(generateRegistrationKeyOrToken(user));
        registration.setSecretToken(generateRegistrationKeyOrToken(user));
        getHibernateStore().save(registration);
        return registration;
    }

    protected String generateRegistrationKeyOrToken(U user) {
        return UUID.randomUUID().toString();
    }

    @Override
    public void save(U user) {
        getHibernateStore().save(user);
    }

    @Override
    public AccountStatus getRegisteredAccountStatus() {
        return registeredAccountStatus;
    }

    @Override
    public List<String> getRegisteredRoles() {
        return registeredRoles;
    }

    @SuppressWarnings("unchecked")
    public T setRegisteredAccountStatus(AccountStatus accountStatus) {
        this.registeredAccountStatus = accountStatus;
        return (T)this;
    }

    @SuppressWarnings("unchecked")
    public T setRegisteredRoles(List<String> roles) {
        this.registeredRoles = roles;
        return (T)this;
    }

}

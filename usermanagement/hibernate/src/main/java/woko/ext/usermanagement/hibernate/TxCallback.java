package woko.ext.usermanagement.hibernate;

import org.hibernate.Session;
import woko.hibernate.HibernateStore;

public interface TxCallback {
    void execute(HibernateStore store, Session session) throws Exception;
}

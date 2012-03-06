package woko.hibernate;

import org.hibernate.Session;
import woko.hibernate.HibernateStore;

public interface TxCallbackWithResult<RES> {
    RES execute(HibernateStore store, Session session) throws Exception;
}

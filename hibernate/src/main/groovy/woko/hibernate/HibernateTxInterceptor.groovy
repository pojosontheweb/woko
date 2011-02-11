package woko.hibernate

import net.sourceforge.stripes.action.Resolution

import net.sourceforge.stripes.controller.ExecutionContext
import net.sourceforge.stripes.controller.Interceptor
import net.sourceforge.stripes.controller.Intercepts
import net.sourceforge.stripes.controller.LifecycleStage

import woko.util.WLogger
import org.hibernate.SessionFactory
import woko.Woko
import org.hibernate.Transaction
import org.hibernate.Session

@Intercepts([LifecycleStage.RequestInit, LifecycleStage.RequestComplete])
class HibernateTxInterceptor implements Interceptor {

  private static final WLogger log = WLogger.getLogger(HibernateTxInterceptor.class)

  private ThreadLocal<Session> sessions = new ThreadLocal<Session>()
  private ThreadLocal<Transaction> transactions = new ThreadLocal<Transaction>()

  private SessionFactory getSessionFactory(ExecutionContext context) {
    Woko woko = Woko.getWoko(context.actionBeanContext.servletContext)
    HibernateStore hs = (HibernateStore)woko.objectStore
    return hs.sessionFactory
  }

  Resolution intercept(ExecutionContext context) throws Exception {
    LifecycleStage stage = context.lifecycleStage
    if (stage==LifecycleStage.RequestInit) {

      Session s = getSessionFactory(context).getCurrentSession()
      sessions.set(s)
      Transaction tx = s.beginTransaction()
      transactions.set(tx)
      log.debug("Started transaction : $tx")

    } else if (stage.equals(LifecycleStage.RequestComplete)) {

      try {
        Transaction tx = transactions.get()
        if (tx==null) {
          log.debug("No transaction found, nothing to do.")
        } else {
          try {
            log.debug("Commiting transaction $tx")
            tx.commit()
          } catch(Exception e) {
            log.error("Commit error : $e", e)
            tx.rollback()
            throw e
          }
        }
      } finally {
        Session s = sessions.get()
        if (s!=null) {
          s.close()
        }
      }
    }

    try {
      return context.proceed();
    } catch(Exception e) {
      log.error("Exception while proceeding with context, rollbacking transaction if any, exception will be rethrown", e)
      Transaction tx = getTx(context)
      if (tx) {
        try {
          tx.rollback()
        } catch(Exception e2) {
          log.error("Exception while rollbacking : $e2", e2)
        }
      }
      // re-throw exception
      throw e
    }
  }

}

package woko2.hibernate

import net.sourceforge.stripes.action.Resolution

import net.sourceforge.stripes.controller.ExecutionContext
import net.sourceforge.stripes.controller.Interceptor
import net.sourceforge.stripes.controller.Intercepts
import net.sourceforge.stripes.controller.LifecycleStage
import org.hibernate.Session

import woko2.util.WLogger
import org.hibernate.SessionFactory
import woko2.Woko
import org.hibernate.Transaction

@Intercepts([LifecycleStage.RequestInit, LifecycleStage.RequestComplete])
class HibernateTxInterceptor implements Interceptor {

  private static final WLogger log = WLogger.getLogger(HibernateTxInterceptor.class)

  private SessionFactory getSessionFactory(ExecutionContext context) {
    Woko woko = Woko.getWoko(context.actionBeanContext.servletContext)
    HibernateStore hs = (HibernateStore)woko.objectStore
    return hs.sessionFactory
  }

  private Transaction getTx(ExecutionContext c) {
    return getSessionFactory(c).currentSession.transaction
  }

  Resolution intercept(ExecutionContext context) throws Exception {
    LifecycleStage stage = context.lifecycleStage
    if (stage==LifecycleStage.RequestInit) {

      Transaction tx = getSessionFactory(context).
              getCurrentSession().
              beginTransaction()
      log.debug("Started transaction : $tx")

    } else if (stage.equals(LifecycleStage.RequestComplete)) {

      Transaction tx = getTx(context)
      if (tx==null) {
        log.debug("No transaction found, nothing to do.")
      } else {
        try {
          log.debug("Commiting transaction $tx")
          tx.commit()
        } catch(Exception e) {
          log.error("Commit error : $e", e)
          throw new RuntimeException(e)
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

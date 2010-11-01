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

  Resolution intercept(ExecutionContext context) throws Exception {
    LifecycleStage stage = context.lifecycleStage
    if (stage==LifecycleStage.RequestInit) {
      log.debug("Request init: Starting new Hibernate Transaction...")
      Transaction tx = getSessionFactory(context).
              getCurrentSession().
              beginTransaction()
      log.debug("... Started transaction : $tx")
    } else if (stage.equals(LifecycleStage.RequestComplete)) {
      log.debug("Request complete : Commiting transaction...")
      Transaction tx = getSessionFactory(context).
              getCurrentSession().
              getTransaction()
      if (tx==null) {
        log.debug("... no transaction found, nothing to do.")
      } else {
        try {
          log.debug("  * Commiting tx $tx")
          tx.commit()
        } catch(Exception e) {
          log.error("Commit error : $e", e)
          throw new RuntimeException(e)
        }
      }
    }

    return context.proceed();
  }

}

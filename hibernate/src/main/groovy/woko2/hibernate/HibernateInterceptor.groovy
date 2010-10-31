package woko2.hibernate

import net.sourceforge.stripes.action.Resolution

import net.sourceforge.stripes.controller.ExecutionContext
import net.sourceforge.stripes.controller.Interceptor
import net.sourceforge.stripes.controller.Intercepts
import net.sourceforge.stripes.controller.LifecycleStage
import org.hibernate.Session

import javax.servlet.http.HttpServletRequest
import woko2.util.WLogger
import org.hibernate.SessionFactory
import woko2.Woko

@Intercepts([LifecycleStage.RequestInit, LifecycleStage.RequestComplete])
class HibernateInterceptor implements Interceptor {

  private static final WLogger log = WLogger.getLogger(HibernateInterceptor.class)

  static final String REQ_ATTR_HB_SESSION = "hibernateSession"

  private SessionFactory getSessionFactory(ExecutionContext context) {
    Woko woko = Woko.getWoko(context.actionBeanContext.servletContext)
    HibernateStore hs = (HibernateStore)woko.objectStore
    return hs.sessionFactory
  }

  Resolution intercept(ExecutionContext context) throws Exception {
    LifecycleStage stage = context.lifecycleStage
    if (stage==LifecycleStage.RequestInit) {
      log.debug("Request Init intercepted, opening new hibernate session...")
      // open up a new session and bind to http request
      Session session = getSessionFactory(context).openSession();
      context.getActionBeanContext().getRequest().setAttribute(REQ_ATTR_HB_SESSION, session);
      log.debug("... Hibernate session created and bound to request");
    } else if (stage.equals(LifecycleStage.RequestComplete)) {
      // close the session for this request
      log.debug("Request Complete intercepted, closing hibernate session...");
      HttpServletRequest request = context.getActionBeanContext().getRequest();
      Session session = (Session) request.getAttribute(REQ_ATTR_HB_SESSION);
      request.removeAttribute(REQ_ATTR_HB_SESSION);
      session.close();
      log.debug("... Hibernate session closed");
    }
    return context.proceed();
  }

}

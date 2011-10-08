package woko.gae;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import woko.util.WLogger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Intercepts({LifecycleStage.RequestInit, LifecycleStage.RequestComplete})
public class GaeEntityManagerInterceptor implements Interceptor {

  private final EntityManagerFactory EMF =
          Persistence.createEntityManagerFactory("transactions-optional");

  private static final WLogger log = WLogger.getLogger(GaeEntityManagerInterceptor.class);

  private final static ThreadLocal<EntityManager> EMF_TL = new ThreadLocal<EntityManager>();

  public static EntityManager getEntityManager() {
    return EMF_TL.get();
  }

  public Resolution intercept(ExecutionContext context) throws Exception {
    LifecycleStage stage = context.getLifecycleStage();
    if (stage==LifecycleStage.RequestInit) {
      EntityManager em = EMF.createEntityManager();
      EMF_TL.set(em);
      log.debug("Obtained entity manager : " + em);
    } else if (stage.equals(LifecycleStage.RequestComplete)) {
      EntityManager em = EMF_TL.get();
      if (em!=null) {
        log.debug("Closed entity managed : " + em);
        em.close();
      }
      EMF_TL.remove();
    }

    return context.proceed();
  }

}

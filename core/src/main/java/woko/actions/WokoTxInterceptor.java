package woko.actions;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import woko.Woko;
import woko.persistence.ObjectStore;
import woko.persistence.StoreTransaction;
import woko.persistence.TransactionalStore;
import woko.util.WLogger;

@Intercepts({LifecycleStage.RequestInit, LifecycleStage.RequestComplete})
public class WokoTxInterceptor implements net.sourceforge.stripes.controller.Interceptor {

    private static final WLogger log = WLogger.getLogger(WokoTxInterceptor.class);

    @Override
    public Resolution intercept(ExecutionContext context) throws Exception {
        Woko<?,?,?,?> woko = Woko.getWoko(context.getActionBeanContext().getServletContext());
        ObjectStore store = woko.getObjectStore();

        if (store instanceof TransactionalStore) {
            // store is transactional, check if a transaction is already open...
            TransactionalStore txStore = (TransactionalStore)store;
            LifecycleStage stage = context.getLifecycleStage();
            if (stage == LifecycleStage.RequestInit) {

                // request init : check if we already have an active transaction and
                // begin a new one if needed
                StoreTransaction tx = txStore.getCurrentTransaction();
                if (tx!=null && !tx.isActive()) {
                    tx = txStore.beginTransaction();
                    if (log.isDebugEnabled()) {
                        log.debug("Started transaction : " + tx);
                    }
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("Transaction already active : " + tx + ", nothing done");
                    }
                }

            } else if (stage.equals(LifecycleStage.RequestComplete)) {

                StoreTransaction tx = txStore.getCurrentTransaction();
                if (tx == null) {
                    if (log.isDebugEnabled()) {
                        log.debug("No transaction found, nothing to do.");
                    }
                } else {
                    if (tx.isActive()) {
                        // check if an error occured
                        Exception wokoException = (Exception)context.getActionBeanContext().getRequest().getAttribute("wokoException");
                        if (wokoException==null) {
                            try {
                                if (log.isDebugEnabled()) {
                                    log.debug("Commiting transaction " + tx);
                                }
                                tx.commit();
                            } catch (Exception e) {
                                log.error("Commit error", e);
                                tx.rollback();
                                throw e;
                            }
                        } else {
                            // error during request execution, roll-back the current transaction
                            log.warn("Exception found in request, roll-backing " + tx);
                            tx.rollback();
                        }
                    }
                }
            }
        }

        try {
            return context.proceed();
        } catch (Exception e) {
            log.error("Exception while proceeding with context, rollbacking transaction if any, exception will be rethrown", e);
            if (store instanceof TransactionalStore) {
                TransactionalStore txStore = (TransactionalStore)store;
                StoreTransaction tx = txStore.getCurrentTransaction();
                if (tx != null && tx.isActive()) {
                    try {
                        tx.rollback();
                    } catch (Exception e2) {
                        log.error("Exception while rollbacking", e2);
                    }
                }
            }
            // re-throw exception
            throw e;
        }

    }
}

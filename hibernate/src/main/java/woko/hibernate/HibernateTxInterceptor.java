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

package woko.hibernate;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import woko.Woko;
import woko.util.WLogger;

@Intercepts({LifecycleStage.RequestInit, LifecycleStage.RequestComplete})
public class HibernateTxInterceptor implements net.sourceforge.stripes.controller.Interceptor {

    private static final WLogger log = WLogger.getLogger(HibernateTxInterceptor.class);

    private SessionFactory getSessionFactory(ExecutionContext context) {
        Woko<HibernateStore,?,?,?> woko = Woko.getWoko(context.getActionBeanContext().getServletContext());
        HibernateStore hs = woko.getObjectStore();
        return hs.getSessionFactory();
    }

    public Resolution intercept(ExecutionContext context) throws Exception {
        LifecycleStage stage = context.getLifecycleStage();
        if (stage == LifecycleStage.RequestInit) {
            Transaction tx = getSessionFactory(context).getCurrentSession().beginTransaction();
            log.debug("Started transaction : " + tx);

        } else if (stage.equals(LifecycleStage.RequestComplete)) {

            Transaction tx = getSessionFactory(context).getCurrentSession().getTransaction();
            if (tx == null) {
                log.debug("No transaction found, nothing to do.");
            } else {
                if (tx.isActive()) {
                    try {
                        log.debug("Commiting transaction " + tx);
                        tx.commit();
                    } catch (Exception e) {
                        log.error("Commit error", e);
                        tx.rollback();
                        throw e;
                    }
                }
            }
        }

        try {
            return context.proceed();
        } catch (Exception e) {
            log.error("Exception while proceeding with context, rollbacking transaction if any, exception will be rethrown", e);
            Session session = getSessionFactory(context).getCurrentSession();
            if (session != null) {
                Transaction tx = session.getTransaction();
                if (tx != null) {
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

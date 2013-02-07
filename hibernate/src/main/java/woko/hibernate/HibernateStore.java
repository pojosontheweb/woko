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

import net.sourceforge.stripes.util.ResolverUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.annotations.Entity;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.metadata.ClassMetadata;
import woko.Closeable;
import woko.persistence.*;
import woko.util.WLogger;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.net.URL;
import java.util.*;

/**
 * Hibernate-backed <code>ObjectStore</code> implementation. Uses Hibernate and JPA to persist the
 * Woko-managed POJOs.
 */
public class HibernateStore implements ObjectStore, TransactionalStore, Closeable {

    public static final String DEFAULT_HIBERNATE_CFG_XML = "/woko_default_hibernate.cfg.xml";

    public static final String CTX_PARAM_PACKAGE_NAMES = "Woko.Hibernate.Packages";

    private static final WLogger log = WLogger.getLogger(HibernateStore.class);

    private final HibernatePrimaryKeyConverter primaryKeyConverter;
    private final SessionFactory sessionFactory;
    private List<Class<?>> mappedClasses;

    /**
     * Create the store with passed packages. Will look for <code>@Entity</code>-annotated classes in specified
     * packaged.
     * If no <code>hibernate.cfg.xml</code> is supplied, will default to
     * <code>woko_default_hibernate.cfg.xml</code>. Simply place a <code>hibernate.cfg.xml</code> to your CLASSPATH
     * root in order to customize the session factory details.
     * @param packageNames a list of packages to scan entities for
     */
    public HibernateStore(List<String> packageNames) {
        log.info("Creating with package names : " + packageNames);
        Configuration cfg = createConfiguration(packageNames);
        log.info("Configuration created, building session factory...");
        URL u = getClass().getResource("/hibernate.cfg.xml");
        if (u == null) {
            String hbCfg = getDefaultHibernateCfgXml();
            log.warn("Using default hibernate settings from " + hbCfg + " : in-memory hsql. Add your own hibernate.cfg.xml to the classpath to change the settings.");
            u = getClass().getResource(hbCfg);
        }
        sessionFactory = cfg.configure(u).buildSessionFactory();
        log.info("Created session factory : " + sessionFactory);
        primaryKeyConverter = createPrimaryKeyConverter();
        log.info("Created PK converter : " + primaryKeyConverter);
        int nbClasses = mappedClasses.size();
        if (nbClasses > 0) {
            log.info(nbClasses + " persistent class(es) added.");
        } else {
            log.warn("No mapped classes found for packages " + packageNames + ". Make sure your @Entity classes are in these packages.");
        }
    }

    /**
     * Return the default <code>woko_default_hibernate.cfg.xml</code> session factory configuration file
     * @return the default session factory config
     */
    protected String getDefaultHibernateCfgXml() {
        return DEFAULT_HIBERNATE_CFG_XML;
    }

    /**
     * Return the hibernate Session Factory
     * @return the Session Factory
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Create and return the primary key converter
     * @return the primary key converter
     */
    protected HibernatePrimaryKeyConverter createPrimaryKeyConverter() {
        return new HibernatePrimaryKeyConverter();
    }

    /**
     * Create the Hibernate <code>Configuration</code> for specified packages
     * @param packageNames the packages to scan for entities
     * @return the Hibernate Configuration
     */
    protected Configuration createConfiguration(List<String> packageNames) {
        mappedClasses = new ArrayList<Class<?>>();
        if (packageNames == null) {
            packageNames = Arrays.asList("model");
        }
        log.info("Creating hibernate annotation configuration");
        AnnotationConfiguration cfg = new AnnotationConfiguration();
        ResolverUtil<Object> resolverUtil = new ResolverUtil<Object>();
        String[] packages = new String[packageNames.size()];
        packages = packageNames.toArray(packages);
        resolverUtil.findAnnotated(Entity.class, packages);
        resolverUtil.findAnnotated(javax.persistence.Entity.class, packages);
        for (Class<?> clazz : resolverUtil.getClasses()) {
            cfg.addAnnotatedClass(clazz);
            mappedClasses.add(clazz);
            log.info("  * " + clazz + " added to config");
        }
        Collections.sort(mappedClasses, new Comparator<Class<?>>() {
            @Override
            public int compare(Class<?> aClass, Class<?> aClass1) {
                return aClass.getSimpleName().compareTo(aClass1.getSimpleName());
            }
        });
        return cfg;
    }

    /**
     * Return the current Hibernate Session
     * @return the current Hibernate Session
     */
    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Load persistent object from the database using the current Hibernate Session.
     * @param className the (mapped) class name of the object to load
     * @param key the key (ID) of the object to load
     * @return the object if found, <code>null</code> otherwise
     */
    public Object load(String className, String key) {
        log.debug("Loading object for className " + className + ", key=" + key);
        if (className == null && key == null) {
            return null;
        }
        Class<?> mappedClass = getMappedClass(className);
        if (mappedClass == null) {
            return null;
        }
        if (key == null) {
            return null;
        }

        Class<?> keyType = getPrimaryKeyClass(mappedClass);
        if (keyType == null) {
            log.warn("Unable to get key type for mappedClass " + mappedClass);
            return null;
        }
        Serializable id = primaryKeyConverter.convert(key, keyType);
        if (id == null) {
            log.warn("Converted key " + key + " to null, unable to load any object");
            return null;
        }
        Session s = getSession();
        Transaction tx = s.getTransaction();
        log.debug("Using transaction " + tx);
        return s.get(mappedClass, id);
    }

    /**
     * Save or update passed object (<code>Session.saveOrUpdate()</code>)
     * @param obj a Woko-managed POJO
     * @return the saved object
     */
    public Object save(Object obj) {
        if (obj == null) {
            return null;
        }
        getSession().saveOrUpdate(obj);
        return obj;
    }

    /**
     * Delete passed object (<code>Session.delete()</code>)
     * @param obj a Woko-managed POJO
     * @return the deleted object
     */
    public Object delete(Object obj) {
        if (obj == null) {
            return null;
        }
        getSession().delete(obj);
        return obj;
    }

    /**
     * Uses primary key converter in order to get the key for passed object
     * @param obj a Woko-managed POJO
     * @return the key for passed object
     */
    public String getKey(Object obj) {
        if (obj == null) {
            return null;
        }
        Serializable k = primaryKeyConverter.getPrimaryKeyValue(sessionFactory, obj, deproxify(obj.getClass()));
        if (k == null) {
            return null;
        }
        return k.toString();
    }

    private Class<?> deproxify(Class<?> clazz) {
        // deproxify if needed
        String className = clazz.getName();
        int i = className.indexOf("_$$_javassist");
        if (i != -1) {
            className = className.substring(0, i);
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                log.error("Error while deproxifying " + clazz, e);
                throw new RuntimeException(e);
            }
        }
        return clazz;
    }

    public String getClassMapping(Class<?> clazz) {
        clazz = deproxify(clazz);
        // is the class persistent ?
        ClassMetadata classMetadata = sessionFactory.getClassMetadata(clazz);
        if (classMetadata == null) {
            // not a hibernated class, return the fully qualified class name
            return clazz.getName();
        } else {
            // hibernated class, return the simple name
            return clazz.getSimpleName();
        }
    }

    public Class<?> getMappedClass(String className) {
        if (className == null) {
            return null;
        }
        // try forName first
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            // lookup simple name
            for (Class<?> clazz : mappedClasses) {
                String simpleName = clazz.getSimpleName();
                if (simpleName.equals(className)) {
                    return clazz;
                }
            }
            return null;
        }
    }

    public Class<?> getPrimaryKeyClass(Class<?> entityClass) {
        ClassMetadata cm = sessionFactory.getClassMetadata(entityClass);
        if (cm == null) {
            // default to String
            return String.class;
        } else {
            return cm.getIdentifierType().getReturnedClass();
        }
    }

    public ResultIterator<?> list(String className, Integer start, Integer limit) {
        Class clazz = getMappedClass(className);
        int s = start == null ? 0 : start;
        int l = limit == null ? -1 : limit;
        if (clazz == null) {
            return new ListResultIterator<Object>(Collections.emptyList(), s, l, 0);
        } else {
            Criteria crit = getSession().createCriteria(clazz).setFirstResult(s);
            if (l != -1) {
                crit.setMaxResults(l);
            }
            // TODO optimize with scrollable results ?
            List<?> objects = crit.list();

            // compute total count
            String mappedClassName = getClassMapping(clazz);
            String query = new StringBuilder("select count(*) from ").append(mappedClassName).toString();
            Long count = (Long) getSession().createQuery(query).list().get(0);

            return new ListResultIterator<Object>(objects, s, l, count.intValue());
        }
    }

    public List<Class<?>> getMappedClasses() {
        return Collections.unmodifiableList(mappedClasses);
    }

    public ResultIterator search(Object query, Integer start, Integer limit) {
        throw new UnsupportedOperationException("Search not implemented in HibernateStore. Override this method to handle full text search.");
    }

    public void close() {
        sessionFactory.close();
    }

    /**
     * Execute passed callback in a Transaction and return the result
     * @param callback the callback to execute
     * @param <RES> the type of the result
     * @return the callback result
     * @deprecated use <code>doInTransactionWithResult</code> instead
     */
    @Deprecated
    public <RES> RES doInTxWithResult(TxCallbackWithResult<RES> callback) {
        Session session = getSessionFactory().getCurrentSession();
        Transaction tx = session.getTransaction();
        if (tx==null || !tx.isActive()) {
            tx = session.beginTransaction();
        }
        try {
            RES res = callback.execute(this, session);
            tx.commit();
            return res;
        } catch(Exception e) {
            tx.rollback();
            throw new RuntimeException(e);
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * Execute passed callback in a Transaction
     * @param callback the callback to execute
     * @deprecated use <code>doInTransaction</code> instead
     */
    @Deprecated
    public void doInTx(TxCallback callback) {
        Session session = getSessionFactory().getCurrentSession();
        Transaction tx = session.getTransaction();
        if (tx==null || !tx.isActive()) {
            tx = session.beginTransaction();
        }
        try {
            callback.execute(this, session);
            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            throw new RuntimeException(e);
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public <RES> RES doInTransactionWithResult(final TransactionCallbackWithResult<RES> callback) {
        return doInTxWithResult(new TxCallbackWithResult<RES>() {
            @Override
            public RES execute(HibernateStore store, Session session) throws Exception {
                return callback.execute();
            }
        });
    }

    @Override
    public void doInTransaction(final TransactionCallback callback) {
        doInTx(new TxCallback() {
            @Override
            public void execute(HibernateStore store, Session session) throws Exception {
                callback.execute();
            }
        });
    }
}

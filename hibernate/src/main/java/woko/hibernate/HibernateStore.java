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

import net.sourceforge.stripes.util.ReflectUtil;
import net.sourceforge.stripes.util.ResolverUtil;
import org.hibernate.*;
import org.hibernate.annotations.Entity;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import woko.Closeable;
import woko.persistence.*;
import woko.util.Util;
import woko.util.WLogger;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
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
    private static final AlternateKeyConverter DEFAULT_ALTERNATE_KEY_CONVERTER = new DefaultAlternateKeyConverter();
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

    protected Object loadObjectWithAlternateKey(Class<?> mappedClass, Util.PropertyNameAndAnnotation<WokoAlternateKey> propAndAltKey, String keyValue) {
        Util.assertArg("mappedClass", mappedClass);
        Util.assertArg("propAndAltKey", propAndAltKey);
        Util.assertArg("keyValue", keyValue);
        WokoAlternateKey alternateKey = propAndAltKey.getAnnotation();
        try {
            return getSession()
                    .createCriteria(mappedClass)
                    .add(Restrictions.eq(alternateKey.altKeyProperty(), keyValue))
                    .setCacheable(true)
                    .uniqueResult();
        } catch (NonUniqueResultException e) {
            // ouch ! several entities with the same altKey...
            String msg = "More than 1 entity found for class " + mappedClass + " with alternate key " + propAndAltKey;
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    private Util.PropertyNameAndAnnotation<WokoAlternateKey> checkForAlternateKey(Class<?> mappedClass) {
        return Util.findAnnotationOnFieldOrAccessor(mappedClass, WokoAlternateKey.class);
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

        Session s = getSession();
        Transaction tx = s.getTransaction();
        if (log.isDebugEnabled()) {
            log.debug("Using transaction " + tx);
        }

        // try actual object ID first...

        Serializable id = primaryKeyConverter.convert(key, keyType);
        if (id == null) {
            if (log.isDebugEnabled()) {
                log.debug("Converted key " + key + " to null, will try alternate key...");
            }
        } else {
            Object o = s.get(mappedClass, id);
            if (o!=null) {
                return o;
            }
        }

        // introspect mapped class and check for @WokoAlternateKey
        Util.PropertyNameAndAnnotation<WokoAlternateKey> alternateKey = checkForAlternateKey(mappedClass);
        if (alternateKey!=null) {
            if (log.isDebugEnabled()) {
                log.debug("@WokoAlternateKey found for mapped class " + mappedClass.getName() + ", querying object.");
            }
            // alternate key found, use this to grab the object
            Object o = loadObjectWithAlternateKey(mappedClass, alternateKey, key);
            if (o!=null) {
                if (log.isDebugEnabled()) {
                    log.debug("Found object for " + mappedClass.getName() + " with alternate key property " + alternateKey);
                }
                return o;
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Object not found for " + mappedClass.getName() + " using alternate key property " + alternateKey + ", will try with ID");
                }
            }
        }

        // no object to load !
        log.warn("Unable to load any object for mapped class " + mappedClass + " and key " + key + ". Will return null.");
        return null;
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

        // alternate key management : we need to set the alternate key on save if
        // @WokoAlternateKey is used
        Class<?> clazz = obj.getClass();
        Util.PropertyNameAndAnnotation<WokoAlternateKey> alternateKey = checkForAlternateKey(clazz);
        if (alternateKey!=null) {
            Object alternateKeyValue = computeAlternateKeyValue(obj, alternateKey);
            if (alternateKeyValue==null) {
                log.warn("Alternate key specified for class " + clazz + " : " + alternateKey + ", but value of the " +
                    "alternateKeyProperty is null. @WokoAlternateKey won't be used for this class.");
            } else {
                WokoAlternateKey annot = alternateKey.getAnnotation();
                PropertyDescriptor pd = ReflectUtil.getPropertyDescriptor(clazz, annot.altKeyProperty());
                Method setter = pd.getWriteMethod();
                if (setter==null) {
                    throw new IllegalStateException("No setter found for alternate key property, could not set alternate key property for class " + clazz + ", alternateKey=" + alternateKey);
                }
                try {
                    setter.invoke(obj, alternateKeyValue);
                } catch (Exception e) {
                    log.error("Exception caught while setting alternate key property, could not set alternate key property for class " + clazz + ", alternateKey=" + alternateKey);
                    throw new RuntimeException(e);
                }
            }
        }

        getSession().saveOrUpdate(obj);
        return obj;
    }

    protected Object computeAlternateKeyValue(Object obj, Util.PropertyNameAndAnnotation<WokoAlternateKey> propAndAnnot) {
        Util.assertArg("obj", obj);
        Util.assertArg("propAndAnnot", propAndAnnot);
        String propName = propAndAnnot.getPropertyName();
        WokoAlternateKey annot = propAndAnnot.getAnnotation();

        Object propValue = Util.getPropertyValue(obj, propName);
        if (propValue==null) {
            // property value is null ! should not happen as the
            // alternateKey-annoted props should always be not null
            // in this case, we just return null, so that alt key is not
            // used
            return null;
        }

        Class<? extends AlternateKeyConverter> converterClass = annot.converter();
        AlternateKeyConverter converter;
        if (converterClass==null) {
            converter = DEFAULT_ALTERNATE_KEY_CONVERTER;
        } else {
            try {
                converter = converterClass.newInstance();
            } catch (Exception e) {
                String msg = "Could not instanciate converter from class " + converterClass;
                log.error(msg, e);
                throw new RuntimeException(e);
            }
        }

        // check unicity of the generated key
        // and compute unique one if needed
        String altKey = converter.convert(obj, propAndAnnot, propName, propValue);
        int counter = 1;
        Class<?> mappedClass = obj.getClass();
        do {
            String uniqueKey = altKey;
            if (counter>1) {
                uniqueKey = uniqueKey + "-" + counter;
            }
            List<?> matching = getSession().createCriteria(mappedClass)
                    .setCacheable(true)
                    .add(Restrictions.eq(propName, uniqueKey))
                    .list();
            if (matching.size()==0) {
                return uniqueKey;
            }
            counter++;
        } while (counter < 1000);

        throw new IllegalStateException("Giving up computation of alternate key for class " + mappedClass + " and alternateKey " + propAndAnnot +
            ". All attempts to generate a key have failed.");

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
        Class<?> mappedClass = getObjectClass(obj);

        // check for alternate key first...
        Util.PropertyNameAndAnnotation<WokoAlternateKey> altKey = checkForAlternateKey(mappedClass);
        if (altKey!=null) {
            // grab the value of the alt key property
            String altKeyProp = altKey.getAnnotation().altKeyProperty();
            Object altKeyVal = Util.getPropertyValue(obj, altKeyProp);
            if (altKeyVal!=null) {
                // TODO what happens for non String props ???
                return altKeyVal.toString();
            }
        }

        // no alternate key was found, use the primary key
        Serializable k = primaryKeyConverter.getPrimaryKeyValue(sessionFactory, obj, mappedClass);
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

        // lookup simple name in our list of mapped classes
        for (Class<?> clazz : mappedClasses) {
            String simpleName = clazz.getSimpleName();
            if (simpleName.equals(className)) {
                return clazz;
            }
        }

        // try forName as a last resort
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.warn("Unable to load mapped class for " + className);
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
            Criteria crit = createListCriteria(clazz).setFirstResult(s);
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

    protected Criteria createListCriteria(Class mappedClass) {
        return getSession().createCriteria(mappedClass);
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
        Session session = getSessionFactory().getCurrentSession();
        Transaction tx = session.getTransaction();
        if (tx==null || !tx.isActive()) {
            tx = session.beginTransaction();
        }
        try {
            RES res = callback.execute();
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

    @Override
    public void doInTransaction(final TransactionCallback callback) {
        Session session = getSessionFactory().getCurrentSession();
        Transaction tx = session.getTransaction();
        if (tx==null || !tx.isActive()) {
            tx = session.beginTransaction();
        }
        try {
            callback.execute();
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
    public StoreTransaction getCurrentTransaction() {
        Session session = getSession();
        Transaction tx = session.getTransaction();
        if (tx==null) {
            return null;
        }
        return new HibernateStoreTransaction(tx);
    }

    @Override
    public StoreTransaction beginTransaction() {
        Session session = getSession();
        Transaction tx = session.beginTransaction();
        return new HibernateStoreTransaction(tx);
    }

    /**
     * Adapter for Hibernate Transactions.
     */
    private class HibernateStoreTransaction implements StoreTransaction {

        private final Transaction hbTx;

        HibernateStoreTransaction(Transaction hbTx) {
            this.hbTx = hbTx;
        }

        @Override
        public boolean isActive() {
            return hbTx.isActive();
        }

        @Override
        public void commit() {
            hbTx.commit();
        }

        @Override
        public void rollback() {
            hbTx.rollback();
        }
    }

    @Override
    public Class<?> getObjectClass(Object o) {
        return deproxyInstance(o).getClass();
    }

    @SuppressWarnings("unchecked")
    protected <T> T deproxyInstance(T maybeProxy) {
        if (maybeProxy instanceof HibernateProxy) {
            HibernateProxy proxy = (HibernateProxy)maybeProxy;
            LazyInitializer i = proxy.getHibernateLazyInitializer();
            return (T) i.getImplementation();
        }
        return maybeProxy;
    }
}

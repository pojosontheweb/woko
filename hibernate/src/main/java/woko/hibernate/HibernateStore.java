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
import woko.persistence.ListResultIterator;
import woko.persistence.ObjectStore;
import woko.persistence.ResultIterator;
import woko.util.WLogger;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.net.URL;
import java.util.*;

public class HibernateStore implements ObjectStore {

    public static final String DEFAULT_HIBERNATE_CFG_XML = "/woko_default_hibernate.cfg.xml";

    public static final String CTX_PARAM_PACKAGE_NAMES = "Woko.Hibernate.Packages";

    private static final WLogger log = WLogger.getLogger(HibernateStore.class);

    private final HibernatePrimaryKeyConverter primaryKeyConverter;
    private final SessionFactory sessionFactory;
    private List<Class<?>> mappedClasses;

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

    protected String getDefaultHibernateCfgXml() {
        return DEFAULT_HIBERNATE_CFG_XML;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    protected HibernatePrimaryKeyConverter createPrimaryKeyConverter() {
        return new HibernatePrimaryKeyConverter();
    }

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

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

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

    public Object save(Object obj) {
        if (obj == null) {
            return null;
        }
        getSession().saveOrUpdate(obj);
        return obj;
    }

    public Object delete(Object obj) {
        if (obj == null) {
            return null;
        }
        getSession().delete(obj);
        return obj;
    }

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


}

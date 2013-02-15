package woko.async.hibernate;

import woko.async.Job;
import woko.async.JobDetailsListener;
import woko.hibernate.HibernateStore;
import woko.persistence.ObjectStore;

/**
 * Hibernate implementation of the job details listener : allows to
 * store job details in the database.
 *
 * Subclasses should override <code>getJobDetailsClass</code> and return their
 * own <code>HbJobDetails</code> subclass for adding field to the job details
 * objects.
 */
public class HibernateJobDetailsListener extends JobDetailsListener {

    private final HibernateStore store;

    public HibernateJobDetailsListener(HibernateStore store) {
        this.store = store;
    }

    @Override
    protected HibernateStore getStore() {
        return store;
    }

    /**
     * Create new <code>HbJobDetails</code> instance using class
     * specified in <code>getJobDetailsClass</code>.
     * @param job the Job instance
     * @return a freshly created <code>HbJobDetails</code> subclass.
     */
    @Override
    protected HbJobDetails createNewJobDetails(Job job) {
        Class<?> clazz = getJobDetailsClass();
        HbJobDetails res;
        try {
            res = (HbJobDetails)clazz.newInstance();
            res.setJobUuid(job.getUuid());
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Return the <code>JobDetails</code> implementation class to be used. This one
     * must extend {@link HbJobDetails}
     * @return the job details implementation class
     */
    @Override
    protected Class<?> getJobDetailsClass() {
        return HbJobDetails.class;
    }
}

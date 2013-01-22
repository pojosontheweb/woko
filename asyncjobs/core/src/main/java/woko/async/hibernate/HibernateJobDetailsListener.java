package woko.async.hibernate;

import woko.async.Job;
import woko.async.JobDetailsListener;
import woko.hibernate.HibernateStore;
import woko.persistence.ObjectStore;

public class HibernateJobDetailsListener extends JobDetailsListener {

    private final HibernateStore store;

    public HibernateJobDetailsListener(HibernateStore store) {
        this.store = store;
    }

    @Override
    protected HibernateStore getStore() {
        return store;
    }

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

    @Override
    protected Class<?> getJobDetailsClass() {
        return HbJobDetails.class;
    }
}

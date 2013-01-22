package woko.async.hibernate;

import woko.async.Job;
import woko.async.JobDetailsListener;
import woko.hibernate.HibernateStore;
import woko.persistence.ObjectStore;

public class HibernateJobDetailsListener<S extends HibernateStore> extends JobDetailsListener {

    private final S store;

    public HibernateJobDetailsListener(S store) {
        this.store = store;
    }

    @Override
    protected ObjectStore getStore() {
        return store;
    }

    @Override
    protected HbJobDetails createNewJobDetails(Job job) {
        Class<HbJobDetails> clazz = getJobDetailsClass();
        HbJobDetails res;
        try {
            res = clazz.newInstance();
            res.setJobUuid(job.getUuid());
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Class<HbJobDetails> getJobDetailsClass() {
        return HbJobDetails.class;
    }
}

package webtests.async

import woko.async.Job
import woko.async.JobDetailsListener
import woko.async.hibernate.HibernateJobDetailsListener
import woko.hibernate.HibernateStore

class MyJobDetailsListener extends HibernateJobDetailsListener {

    MyJobDetailsListener(HibernateStore store) {
        super(store)
    }

    @Override
    protected Class<MyJobDetails> getJobDetailsClass() {
        return MyJobDetails.class
    }

    @Override
    void onProgress(Job job) {
        doInTxIfNeeded(getStore(), {
            MyJobDetails d = (MyJobDetails)getJobDetails(job)
            MyJob j = (MyJob)job
            d.current = j.current
        } as JobDetailsListener.Callback)
    }
}

package webtests.async

import woko.async.hibernate.HibernateJobDetailsListener
import woko.hibernate.HibernateStore

class MyJobDetailsListener extends HibernateJobDetailsListener {

    MyJobDetailsListener(HibernateStore store) {
        super(store)
    }

    @Override
    protected Class<?> getJobDetailsClass() {
        return MyJobDetails.class
    }

}

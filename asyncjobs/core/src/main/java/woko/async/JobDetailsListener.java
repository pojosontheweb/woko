package woko.async;

import woko.persistence.ObjectStore;
import woko.persistence.TransactionCallback;
import woko.persistence.TransactionalStore;

/**
 * Listener base class that allows to automatically update Woko-managed {@link JobDetails} instances
 * based on {@link Job} execution.
 * Bridges notifications from the job execution on <code>JobDetails.updateXxx()</code> methods.
 */
public abstract class JobDetailsListener extends JobAdapter {

    public static interface Callback {
        void execute();
    }

    protected void doInTxIfNeeded(final ObjectStore store, final Callback c) {
        if (store instanceof TransactionalStore) {
            ((TransactionalStore) store).doInTransaction(new TransactionCallback() {
                @Override
                public void execute() throws Exception {
                    c.execute();
                }
            });
        } else {
            c.execute();
        }
    }

    protected abstract ObjectStore getStore();

    protected abstract JobDetails createNewJobDetails(Job job);

    protected abstract Class<?> getJobDetailsClass();

    protected JobDetails getJobDetails(Job job) {
        return (JobDetails)getStore().load(getStore().getClassMapping(getJobDetailsClass()), job.getUuid());
    }

    @Override
    public void onStart(final Job job) {
        doInTxIfNeeded(getStore(), new Callback() {
            @Override
            public void execute() {
                JobDetails details = createNewJobDetails(job);
                details.updateStarted(job);
                getStore().save(details);
            }
        });
    }

    @Override
    public void onProgress(final Job job) {
        doInTxIfNeeded(getStore(), new Callback() {
            @Override
            public void execute() {
                JobDetails details = getJobDetails(job);
                if (details!=null) {
                    if (details.updateProgress(job)) {
                        getStore().save(details);
                    }
                }
            }
        });
    }


    @Override
    public void onException(final Job job, final Exception e) {
        doInTxIfNeeded(getStore(), new Callback() {
            @Override
            public void execute() {
                JobDetails details = getJobDetails(job);
                if (details!=null) {
                    details.updateException(e, job);
                    getStore().save(details);
                }
            }
        });
    }

    @Override
    public void onEnd(final Job job, final boolean killed) {
        doInTxIfNeeded(getStore(), new Callback() {
            @Override
            public void execute() {
                JobDetails details = getJobDetails(job);
                if (details!=null) {
                    details.updateEnded(job);
                    if (killed) {
                        details.updateKilled(job);
                    }
                    getStore().save(details);
                }
            }
        });
    }
}

package woko.async;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JobManager {

    private final ExecutorService pool;

    public JobManager() {
        this(Executors.newFixedThreadPool(1));
    }

    public JobManager(ExecutorService pool) {
        this.pool = pool;
    }

    public void submit(final Job job, final List<JobListener> listeners) {
        pool.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                job.execute(listeners);
                return null;
            }
        });
    }
}

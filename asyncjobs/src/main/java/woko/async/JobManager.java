package woko.async;

import woko.Closeable;
import woko.util.WLogger;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JobManager implements Closeable {

    private static final WLogger logger = WLogger.getLogger(JobManager.class);

    private final ExecutorService pool;

    public JobManager() {
        this(Executors.newFixedThreadPool(1));
    }

    public JobManager(ExecutorService pool) {
        logger.info("Created with ExecutorService : " + pool);
        this.pool = pool;
    }

    public void submit(final Job job, final List<JobListener> listeners) {
        logger.debug("Submitting Job : " + job + " with listeners : " + listeners);
        pool.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                job.execute(listeners);
                return null;
            }
        });
    }

    public void close() {
        logger.debug("Closing pool : " + pool);
        pool.shutdown();
    }
}

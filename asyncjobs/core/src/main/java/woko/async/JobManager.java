package woko.async;

import woko.Closeable;
import woko.util.WLogger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JobManager implements Closeable {

    public static final String KEY = "MailService";

    private static final WLogger logger = WLogger.getLogger(JobManager.class);

    private final ExecutorService pool;
    private final Map<String,Job> runningJobs = new ConcurrentHashMap<String, Job>();

    public JobManager() {
        this(Executors.newSingleThreadExecutor());
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
                runningJobs.put(job.getUuid(), job);
                try {
                    job.execute(listeners);
                    return null;
                } finally {
                    runningJobs.remove(job.getUuid());
                }
            }
        });
    }

    public void close() {
        logger.debug("Closing pool : " + pool);
        pool.shutdown();
    }

    public Job getRunningJob(String uuid) {
        return runningJobs.get(uuid);
    }
}

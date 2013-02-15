package woko.async;

import woko.Closeable;
import woko.util.WLogger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Top-level component for managing async {@link Job}s.
 */
public class JobManager implements Closeable {

    public static final String KEY = "JobManager";

    private static final WLogger logger = WLogger.getLogger(JobManager.class);

    private final ExecutorService pool;
    private final Map<String,Job> runningJobs = new ConcurrentHashMap<String, Job>();

    /**
     * Create with a single-threaded executor.
     */
    public JobManager() {
        this(Executors.newSingleThreadExecutor());
    }

    /**
     * Create with passed <code>ExecutorService</code>.
     * @param pool the <code>ExecutorService</code> to be used
     */
    public JobManager(ExecutorService pool) {
        logger.info("Created with ExecutorService : " + pool);
        this.pool = pool;
    }

    /**
     * Submit and execute a new <code>Job</code>, and notify all listeners.
     * @param job the <code>Job</code> to execute
     * @param listeners a list of the listeners to be notified during job execution
     */
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

    /**
     * Shutdown the <code>ExecutorService</code> passed at construction time.
     */
    public void close() {
        logger.debug("Closing pool : " + pool);
        pool.shutdown();
    }

    /**
     * Return a running <code>Job</code> by its <code>uuid</code>. Returns <code>null</code> if
     * no job is currently running with passed uuid.
     * @param uuid the uuid of the <code>Job</code> to retrieve
     * @return the <code>Job</code> if any, <code>null</code> otherwise
     */
    public Job getRunningJob(String uuid) {
        return runningJobs.get(uuid);
    }
}

package woko.async;

import java.util.List;

/**
 * Base interface for asynchronous, background-style jobs.
 * Concrete subclasses should implement the logic of the job in the
 * <code>execute</code> method.
 */
public interface Job {

    /**
     * Return the UUID for the job
     */
    String getUuid();

    /**
     * Execute the job, possibly in a separate thread. Job execution should notify
     * passed listeners on the various steps.
     * @param listeners a list of job listeners to notify on execution of the job
     */
    void execute(List<JobListener> listeners);

    /**
     * Kill the job. When this method is invoked, the job should stop its work
     * as soon as possible.
     * Listeners passed to <code>execute</code> should be notified of the kill.
     */
    void kill();

}

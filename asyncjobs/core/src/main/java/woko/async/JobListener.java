package woko.async;

/**
 * Listeners for {@link Job} execution notifications.
 */
public interface JobListener {

    /**
     * Invoked when the job has started
     * @param job the job
     */
    void onStart(Job job);

    /**
     * Invoked when the job has progressed
     * @param job the job
     */
    void onProgress(Job job);

    /**
     * Invoked when the job has throw an excption
     * @param job the job
     * @param e the exception
     */
    void onException(Job job, Exception e);

    /**
     * Invoked when the job has ended normally or has been killed
     * @param job the job
     * @param killed <code>true</code> if the job was killed, <code>false</code> otherwise
     */
    void onEnd(Job job, boolean killed);

}

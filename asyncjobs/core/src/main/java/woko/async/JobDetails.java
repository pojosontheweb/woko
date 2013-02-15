package woko.async;

import java.util.Date;

/**
 * Base interface for Job Details.
 *
 * JobDetails represent the state of an async {@link Job}. Such objects can be
 * used in order to monitor <code>Job</code> execution.
 */
public interface JobDetails {

    /**
     * Return the job uuid
     */
    String getJobUuid();

    /**
     * Invoked when passed <code>Job</code> has progressed.
     * @param job the job
     * @return <code>true</code> if this <code>JobDetails</code> was modified by the call, <code>false</code> otherwise
     */
    boolean updateProgress(Job job);

    /**
     * Invoked when passed <code>Job</code> has thrown an <code>Exception</code>.
     * @param e the exception thrown by the job
     * @param job the job
     */
    void updateException(Exception e, Job job);

    /**
     * Invoked when passed <code>Job</code> has ended.
     * @param job the job
     */
    void updateEnded(Job job);

    /**
     * Invoked when passed <code>Job</code> was killed.
     * @param job the job
     */
    void updateKilled(Job job);

    /**
     * Invoked when passed <code>Job</code> has started.
     * @param job the job
     */
    void updateStarted(Job job);

    /**
     * Return the start time for the job
     */
    Date getStartTime();

    /**
     * Return the end time for the job if any
     */
    Date getEndTime();

    /**
     * Return the kill time if any
     */
    Date getKillTime();

    /**
     * Return the exception string if any
     */
    String getExceptionString();
}

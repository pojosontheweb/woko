package woko.async;

import woko.mock.MockUtil;
import woko.util.WLogger;

import java.util.List;
import java.util.UUID;

/**
 * Base abstract class for async jobs. Handles UUID and listeners notification for
 * <code>start</code>, <code>end</code>, and <code>exception</code>.
 *
 * @see JobWithProgressBase
 */
public abstract class JobBase implements Job {

    private static final WLogger logger = WLogger.getLogger(JobBase.class);

    private final String uuid = UUID.randomUUID().toString();

    public String getUuid() {
        return uuid;
    }

    protected boolean isKilled() {
        return false;
    }

    /**
     * Does nothing. To be overriden in order to manage kill.
     */
    @Override
    public void kill() {
    }

    protected final void notifyListenersStart(List<JobListener> listeners) {
        for (JobListener l : listeners) {
            try {
            } catch(Exception e) {
                logger.error("Caught exception invoking listener " + l, e);
            }
        }
    }

    protected final void notifyListenersEnd(List<JobListener> listeners) {
        for (JobListener l : listeners) {
            try {
                l.onEnd(this, isKilled());
            } catch(Exception e) {
                logger.error("Caught exception invoking listener " + l, e);
            }
        }
    }

    protected final void notifyListenersException(List<JobListener> listeners, Exception e) {
        for (JobListener l : listeners) {
            try {
                l.onException(this, e); // Exception onException... looks pretty ugly but we need to guard against bad code anyway...
            } catch(Exception e2) {
                logger.error("Caught exception invoking listener " + l, e2);
            }
        }
    }

    protected final void notifyListenersProgress(List<JobListener> listeners) {
        for (JobListener l : listeners) {
            try {
                l.onProgress(this);
            } catch(Exception e) {
                logger.error("Caught exception invoking listener " + l, e);
            }
        }
    }

    /**
     * Wraps invocation of <code>doExecute</code> with calls to listeners.
     * @param listeners a list of job listeners to notify on execution of the job
     */
    @Override
    public final void execute(List<JobListener> listeners) {
        logger.info("Starting " + this);
        notifyListenersStart(listeners);
        try {
            doExecute(listeners);
            logger.info("Executed " + this);
            notifyListenersEnd(listeners);
        } catch(Exception e) {
            logger.error("Caught exception executing " + this, e);
            notifyListenersException(listeners, e);
        }
    }

    /**
     * Template method to be implemented by subclasses. Place job logic here.
     * @param listeners the listeners to be invoked for events not handled by this class (e.g. progress)
     */
    protected abstract void doExecute(List<JobListener> listeners);

}

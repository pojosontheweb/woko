package woko.async;

import woko.util.WLogger;

import java.util.List;

public abstract class JobBase implements Job {

    private static final WLogger logger = WLogger.getLogger(JobBase.class);

    @Override
    public void execute(List<JobListener> listeners) {
        for (JobListener l : listeners) {
            try {
                l.onStart(this);
            } catch(Exception e) {
                logger.error("Caught exception invoking listener " + l, e);
            }
        }
        try {
            doExecute(listeners);
            for (JobListener l : listeners) {
                try {
                    l.onEnd(this);
                } catch(Exception e) {
                    logger.error("Caught exception invoking listener " + l, e);
                }
            }
        } catch(Exception e) {
            for (JobListener l : listeners) {
                try {
                    l.onException(this, e); // Exception onException... looks pretty ugly but we need to guard against bad code anyway...
                } catch(Exception e2) {
                    logger.error("Caught exception invoking listener " + l, e2);
                }
            }
        }
    }

    protected abstract void doExecute(List<JobListener> listeners);
}

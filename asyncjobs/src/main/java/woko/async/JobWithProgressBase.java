package woko.async;

import woko.util.WLogger;

import java.util.List;

public abstract class JobWithProgressBase extends JobBase {

    private static final WLogger logger = WLogger.getLogger(JobWithProgressBase.class);

    @Override
    protected void doExecute(List<JobListener> listeners) {
        while (hasNextStep()) {
            for (JobListener l : listeners) {
                try {
                    l.onProgress(this);
                } catch(Exception e) {
                    logger.error("Caught exception invoking listener " + l, e);
                }
            }
            doExecuteNextStep();
        }
    }

    protected abstract void doExecuteNextStep();

    protected abstract boolean hasNextStep();
}

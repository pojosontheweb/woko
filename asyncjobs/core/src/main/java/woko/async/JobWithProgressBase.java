package woko.async;

import woko.util.WLogger;

import java.util.List;

public abstract class JobWithProgressBase extends JobBase {

    private static final WLogger logger = WLogger.getLogger(JobWithProgressBase.class);

    private boolean killRequired = false;
    private boolean killed = false;

    @Override
    protected void doExecute(List<JobListener> listeners) {
        while (hasNextStep()) {
            if (killRequired) {
                logger.info("Kill required for " + this);
                killed = true;
                break;
            } else {
                doExecuteNextStep();
                notifyListenersProgress(listeners);
            }
        }
    }

    @Override
    protected boolean isKilled() {
        return killed;
    }

    @Override
    public void kill() {
        logger.info("Killing " + this);
        killRequired = true;
    }

    protected boolean isKillRequired() {
        return killRequired;
    }

    protected abstract void doExecuteNextStep();

    protected abstract boolean hasNextStep();
}

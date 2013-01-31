package woko.async;

/**
 * Adapter for JobListener.
 */
public class JobAdapter implements JobListener {

    @Override
    public void onStart(Job job) {
    }

    @Override
    public void onProgress(Job job) {
    }

    @Override
    public void onException(Job job, Exception e) {
    }

    @Override
    public void onEnd(Job job, boolean killed) {
    }
}

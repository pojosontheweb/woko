package webtests.async

import woko.async.JobWithProgressBase

class MyJob extends JobWithProgressBase {

    private final int max;
    int current = 0

    MyJob(int max) {
        this.max = max
    }

    int getMax() {
        return max
    }

    int getCurrent() {
        return current
    }

    @Override
    protected void doExecuteNextStep() {
        current++
        Thread.sleep(100);
    }

    @Override
    protected boolean hasNextStep() {
        return current < max;
    }
}

package woko.async

import org.junit.Test
import static org.junit.Assert.*

class JobManagerTest {

    private void assertListenerStartedAndSleep(def start, MyJobListener listener) {
        println "Waiting for start..."
        while (System.currentTimeMillis()-start<500 && !listener.started) {
            Thread.sleep(100)
        }
        assert listener.started
        assert !listener.ended
        println "started, sleeping for max 10 secs..."
        while (System.currentTimeMillis()-start < 10000 && !listener.ended) {
            Thread.sleep(1000)
            print "z"
        }
        println "\nback to life !"
    }

    @Test
    void testSimpleJob() {
        JobManager jm = new JobManager()
        Job simple = new MySimpleJob()
        MyJobListener listener = new MyJobListener();
        def start = System.currentTimeMillis()
        jm.submit(simple, [listener])
        assertListenerStartedAndSleep(start, listener)
        assert listener.ended
    }

    @Test
    void testJobWithProgress() {
        JobManager jobManager = new JobManager()
        Job myJob = new MyJobWithProgress()
        def start = System.currentTimeMillis()
        MyJobListener listener = new MyJobListener();
        jobManager.submit(myJob, [listener])
        assertListenerStartedAndSleep(start, listener)
        assert listener.ended
        assert listener.nbProgress == 10
    }

}

class MySimpleJob extends JobBase {

    boolean done = false

    @Override
    protected void doExecute(List<JobListener> listeners) {
        Thread.sleep(5000)
        done = true
    }
}

class MyJobWithProgress extends JobWithProgressBase {

    int i = 0

    @Override
    protected void doExecuteNextStep() {
        i++
        Thread.sleep(500)
    }

    @Override
    protected boolean hasNextStep() {
        i < 10
    }

}

class MyJobListener extends JobAdapter {

    boolean started = false
    boolean ended = false
    int nbProgress = 0

    @Override
    void onStart(Job job) {
        started = true
    }

    @Override

    void onProgress(Job job) {
        nbProgress++
    }

    @Override
    void onException(Job job, Exception e) {
        e.printStackTrace()
        fail("Caught exception " + e)
    }

    void onEnd(Job job) {
        ended = true
    }

}

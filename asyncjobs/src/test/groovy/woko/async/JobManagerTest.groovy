package woko.async

import org.junit.Ignore
import org.junit.Test
import static org.junit.Assert.*

class JobManagerTest {

    private def now() {
        System.currentTimeMillis()
    }

    private void assertListenerStarted(def start, MyJobListener listener) {
        println "Waiting for start..."
        while (now()-start<500 && !listener.started) {
            Thread.sleep(100)
        }
        assert listener.started
        assert !listener.ended
        println "started"
    }

    private void assertListenerStartedAndSleep(def start, MyJobListener listener) {
        assertListenerStarted(start, listener)
        println "sleeping for max 10 secs..."
        while (now()-start < 10000 && !listener.ended) {
            Thread.sleep(1000)
            print "z"
        }
        println "\nback to life !"
    }

    private void doWithTestObjects(Closure c) {
        JobManager jm = new JobManager()
        MyJobListener listener = new MyJobListener();
        def start = now()
        try {
            c.call(jm, listener)
        } finally {
            jm.close()
        }
    }

    @Test
    void testSimpleJob() {
        doWithTestObjects { JobManager jm, MyJobListener listener ->
            Job simple = new MySimpleJob()
            def start = now()
            jm.submit(simple, [listener])
            assertListenerStartedAndSleep(start, listener)
            assert listener.ended
        }
    }

    @Test
    void testJobWithProgress() {
        doWithTestObjects { JobManager jm, MyJobListener listener ->
            Job myJob = new MyJobWithProgress()
            def start = now()
            jm.submit(myJob, [listener])
            assertListenerStartedAndSleep(start, listener)
            assert listener.ended
            assert listener.nbProgress == 10
        }
    }

    @Test
    @Ignore
    void testJobKill() {
        doWithTestObjects { JobManager jm, MyJobListener listener ->
            Job myJob = new MyJobWithProgress()
            def start = now()
            assertListenerStarted(start, listener)
            j.kill()
            Thread.sleep(2000)
            assert listener.killed
        }
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

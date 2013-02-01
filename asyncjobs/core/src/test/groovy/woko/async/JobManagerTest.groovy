package woko.async

import org.junit.Test

import java.util.concurrent.Executors

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
            Job simple = new MySimpleJob(jobId: "simple")
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
    void testJobKill() {
        doWithTestObjects { JobManager jm, MyJobListener listener ->
            Job myJob = new MyJobWithProgress()
            def start = now()
            jm.submit(myJob, [listener])
            assertListenerStarted(start, listener)
            myJob.kill()
            Thread.sleep(2000)
            assert listener.killed
        }
    }


    @Test
    void testJobConcurrent() {
        JobManager jm = new JobManager(Executors.newFixedThreadPool(5))
        def start = now()
        try {

            // start concurrent jobs
            def listeners = []
            for (def i in 1..5) {
                def l = new MyJobListener()
                listeners << l
                MySimpleJob j = new MySimpleJob(jobId:"simple$i")
                jm.submit(j, [l])
            }

            // assert all jobs have been started
            boolean allStarted = false
            while (now()-start<10000 && !allStarted) {
                boolean hasNotStarted = false
                for (MyJobListener l : listeners) {
                    if (!l.started) {
                        hasNotStarted = true
                        break
                    }
                }
                allStarted = !hasNotStarted
            }
            assert allStarted

            // assert all jobs have finished before timeout
            boolean allFinished = false
            while (now()-start<10000 && !allFinished) {
                boolean hasNotFinished = false
                for (MyJobListener l : listeners) {
                    if (!l.ended) {
                        hasNotFinished = true
                        break
                    }
                }
                allFinished = !hasNotFinished
            }
            assert allFinished
        } finally {
            jm.close()
        }
    }

    @Test
    void testJobConcurrentSerialized() {
        JobManager jm = new JobManager(Executors.newFixedThreadPool(1)) // 1 thread only ! will serialize...
        def start = now()
        try {

            // start 2 jobs
            List<MyJobListener> listeners = []
            for (def i in 1..2) {
                def l = new MyJobListener()
                listeners << l
                MySimpleJob j = new MySimpleJob(jobId:"simple$i")
                jm.submit(j, [l])
            }

            // first one should have blocked the second so we should have
            // ended the first before the second
            boolean firstEndedSecondStarted = false
            while (now()-start<10000 && !firstEndedSecondStarted) {
                Thread.sleep(100)
                firstEndedSecondStarted = listeners[0].ended && listeners[1].started
            }
            assert firstEndedSecondStarted

            // wait for second thread to end
            boolean secondEnded = false
            while (now()-start<20000 && !secondEnded) {
                Thread.sleep(100)
                secondEnded = listeners[1].ended
            }
            assert secondEnded

        } finally {
            jm.close()
        }
    }


}

class MySimpleJob extends JobBase {

    boolean done = false
    def jobId

    @Override
    protected void doExecute(List<JobListener> listeners) {
        println "Hey $jobId"
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
    boolean killed = false

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

    @Override
    void onEnd(Job job, boolean killed) {
        ended = true
        this.killed = killed
    }

}

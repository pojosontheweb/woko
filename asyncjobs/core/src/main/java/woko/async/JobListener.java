package woko.async;

public interface JobListener {

    void onStart(Job job);
    void onProgress(Job job);
    void onException(Job job, Exception e);
    void onEnd(Job job, boolean killed);

}

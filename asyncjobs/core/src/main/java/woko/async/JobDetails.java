package woko.async;

public interface JobDetails {

    String getJobUuid();

    boolean updateProgress(Job job);

    void updateException(Exception e, Job job);

    void updateEnded(Job job);

    void updateKilled(Job job);

}

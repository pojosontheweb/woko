package woko.async;

import java.util.Date;

public interface JobDetails {

    String getJobUuid();

    boolean updateProgress(Job job);

    void updateException(Exception e, Job job);

    void updateEnded(Job job);

    void updateKilled(Job job);

    void updateStarted(Job job);

    Date getStartTime();

    Date getEndTime();

    Date getKillTime();

    String getExceptionString();
}

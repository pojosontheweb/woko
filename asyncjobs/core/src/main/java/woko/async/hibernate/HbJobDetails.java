package woko.async.hibernate;

import woko.async.Job;
import woko.async.JobDetails;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class HbJobDetails implements JobDetails {

    @Id
    private String jobUuid;

    private Date startTime = new Date();
    private Date endTime = null;
    private Date killTime = null;
    private String exceptionString = null;

    public String getJobUuid() {
        return jobUuid;
    }

    void setJobUuid(String jobUuid) {
        this.jobUuid = jobUuid;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public Date getKillTime() {
        return killTime;
    }

    public String getExceptionString() {
        return exceptionString;
    }

    @Override
    public boolean updateProgress(Job job) {
        return false;
    }

    @Override
    public void updateException(Exception e, Job job) {
        exceptionString = e.toString();
    }

    @Override
    public void updateEnded(Job job) {
        endTime = new Date();
    }

    @Override
    public void updateKilled(Job job) {
        killTime = new Date();
    }

}

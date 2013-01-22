package woko.async;

import java.util.List;

public interface Job {

    void execute(List<JobListener> listeners);

}

package woko.async;

import java.util.List;

public interface Job {

    String getUuid();

    void execute(List<JobListener> listeners);

    void kill();

}

package woko.push;

import java.util.Collections;
import java.util.List;

public class PushResult {

    private final List<PushedSourceResult> pushedSourceResults;

    public PushResult(List<PushedSourceResult> pushedSourceResults) {
        this.pushedSourceResults = Collections.unmodifiableList(
          pushedSourceResults==null ? Collections.<PushedSourceResult>emptyList() : pushedSourceResults);
    }

    public List<PushedSourceResult> getPushedSourceResults() {
        return pushedSourceResults;
    }
}

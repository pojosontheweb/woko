package woko.tooling.cli.commands

import woko.tooling.cli.Runner
import woko.tooling.utils.Logger

class StartCommand extends ProcessCmd{

    private boolean started = false

    StartCommand(Runner runner) {
        super(runner,
                "start",
                "run the application in a local jetty container",
                "",
                "",
                "mvn jetty:run-exploded")
    }

    @Override
    protected void handleLine(String line) {
        super.handleLine(line)
        if (!started) { // no need to do this for each line when the app has started
            if (line =~ /Starting scanner/) {
                started = true
                iLog("")
                iLog("Your application has been deployed at http://localhost:8080/$artifactId")
                iLog("ctrl-c to stop container or woko stop")
                iLog("")
            }
        }
    }


}

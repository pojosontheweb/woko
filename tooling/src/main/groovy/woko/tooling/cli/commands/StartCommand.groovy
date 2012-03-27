package woko.tooling.cli.commands

import woko.tooling.cli.Runner
import woko.tooling.utils.Logger

class StartCommand extends ProcessCmd{

    private boolean started = false

    StartCommand(Runner runner, File projectDir, Logger logger, String name, String shortDesc, String argSpec, String longHelp, String commandLine) {
        super(runner, projectDir, logger, name, shortDesc, argSpec, longHelp, commandLine)
    }

    @Override
    void execute(List<String> args) {
        super.execute(args)
        iLog("Your application has been deployed : http://localhost:8080/$artifactId")
        iLog("Press 'Ctrl-c' or 'woko stop' to stop the container")
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

package woko.tooling.cli.commands

import woko.tooling.cli.Runner
import woko.tooling.utils.Logger

class StartCommand extends ProcessCmd{

    StartCommand(Runner runner, File projectDir, Logger logger, String name, String shortDesc, String argSpec, String longHelp) {
        super(runner, projectDir, logger, name, shortDesc, argSpec, longHelp, "mvn package jetty:run-exploded")
    }

    @Override
    void execute(List<String> args) {
        super.execute(args)
        iLog("Your application has been deployed : http://localhost:8080/$artifactId")
        iLog("Press 'Ctrl-c' or 'woko stop' to stop the container")
    }

}

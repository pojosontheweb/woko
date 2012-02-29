package woko.tooling.cli.commands

import woko.tooling.cli.Command
import woko.tooling.cli.Runner
import woko.tooling.utils.Logger

class ProcessCmd extends Command {

    private final String commandLine

    ProcessCmd(Runner runner, File projectDir, Logger logger, String name, String shortDesc, String argSpec, String longHelp, String commandLine) {
        super(runner, projectDir, logger, name, shortDesc, argSpec, longHelp)
        this.commandLine = commandLine
    }

    @Override
    void execute(List<String> args) {
        logger.indentedLog(" Launching process : $commandLine")
        Process p = commandLine.execute()
        p.in.eachLine { logger.indentedLog(it) }
        p.waitFor()
        logger.indentedLog(" Process terminated, returned ${p.exitValue()}")
    }


}

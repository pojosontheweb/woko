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

    boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase()
		return (os.indexOf("win") >= 0)
    }

    @Override
    void execute(List<String> args) {
        String cmd = windows ?  "cmd /c $commandLine" : commandLine
        logger.indentedLog(" Launching process : $cmd")
        Process p = cmd.execute()
        p.in.eachLine { logger.indentedLog(it) }
        p.waitFor()
        logger.indentedLog(" Process terminated for $name, returned ${p.exitValue()}")
    }


}

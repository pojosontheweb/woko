package woko.tooling.cli

import woko.tooling.utils.Logger
import woko.Woko
import woko.WokoInitListener
import net.sourceforge.jfacets.FacetDescriptor
import net.sourceforge.jfacets.IFacetDescriptorManager
import static woko.tooling.utils.AppUtils.*
import woko.facets.builtin.WokoFacets
import woko.facets.FragmentFacet
import woko.tooling.utils.PomHelper
import woko.tooling.cli.commands.ListCmd
import woko.tooling.cli.commands.CreateCmd
import woko.tooling.cli.commands.ProcessCmd

class Runner {

    private Logger logger

    private Map<String,Command> commands = [:]

    private File workingDir = new File(System.getProperty("user.dir"))

    Runner addCommand(Command c) {
        if (!commands[c]) {
            commands[c.name] = c
        }
        return this
    }

    Runner addCommands(List<Command> commands) {
        commands?.each {
            addCommand(it)
        }
        return this
    }

    private void iLog(msg) {
        logger.indentedLog(msg)
    }

    private void log(msg) {
        logger.log(msg)
    }

    void help(String commandName) {
        if (commandName) {
            def c = commands[commandName]
            if (c) {
                log("Help for command '$commandName' : $c.shortDesc")
                log("\nUsage :\n")
                log(" - woko $commandName $c.argSpec")
                if (c.longHelp) {
                    log("\n$c.longHelp")
                }
            } else {
                log("No such command : $commandName")
            }
        } else {
            log("Usage : woko <command> arg*")
            log("\nAvailable commands :\n")
            commands.each { k, v ->
                log("  - $k $v.argSpec\t\t:\t\t$v.shortDesc")
            }
            log("  - help [command_name]\t\t:\t\tdisplay help about specified command")
            log(" ")
        }
    }

    Runner(Logger logger, File workingDir) {
        this.logger = logger
        this.workingDir = workingDir
        addCommands([
            new ListCmd(this, workingDir, logger),
            new CreateCmd(this, workingDir, logger),
            new ProcessCmd(
                    this,
                    workingDir,
                    logger,
                    "start",
                    "run the application in a local tomcat container",
                    "",
                    "",
                    "mvn package cargo:start -Dmaven.test.skip"
            ),
            new ProcessCmd(
                    this,
                    workingDir,
                    logger,
                    "build",
                    "rebuilds the whole application",
                    "",
                    "",
                    "mvn clean install"
            )

        ])
    }

    void invokeCommand(args) {
        if (!args) {
            throw new IllegalArgumentException("0 args specified, we need at least the command name")
        }
        if (args[0]=="help") {
            help()
        } else {
            def command = commands[args[0]]
            if (!command) {
                throw new IllegalArgumentException("command ${args[0]} does not exist")
            }
            def commandArgs = []
            if (args) {
                boolean first = true
                args.each { arg ->
                    if (first) {
                        first = false
                    } else {
                        commandArgs << arg
                    }
                }
            }
            command.execute(commandArgs)
        }
    }

    /**
     * Run with passed arguments
     * @param args an array or list of Strings containing the command line parameters
     */
    void run(args) {
        // Display home msg
        logger.splashMsg()
        args = args ? args : ["help"]
        invokeCommand(args)
    }

    public static void main(String[] args) {
        String currentDir = System.getProperty("user.dir")
        if (!currentDir) {
            throw new IllegalStateException("Cannot determine current dir (user.dir sys prop not found)")
        }
        Writer out = new OutputStreamWriter(System.out)
        try {
            new Runner(new Logger(out), new File(currentDir)).run(args)
        } finally {
            out.flush()
            out.close()
        }
   }

}

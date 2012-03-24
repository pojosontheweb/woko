/*
 * Copyright 2001-2010 Remi Vankeisbelck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package woko.tooling.cli

import woko.tooling.utils.Logger
import woko.tooling.cli.commands.*

class Runner {

    private Logger logger

    private Map<String, Command> commands = [:]

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
            log("")
        }
    }

    Runner(Logger logger, File workingDir) {
        this.logger = logger
        this.workingDir = workingDir
        addCommands([
          new ListCmd(this, workingDir, logger),
          new CreateCmd(this, workingDir, logger),
          new CrudCmd(this, workingDir, logger),
          new PushCmd(this, workingDir, logger),
          new GenerateCmd(this, workingDir, logger),
          new StartCommand(
            this,
            workingDir,
            logger,
            "start",
            "run the application in a local jetty container",
            "",
            "",
            "mvn package jetty:run-exploded -Dmaven.test.skip",
          ),
          new ProcessCmd(
            this,
            workingDir,
            logger,
            "stop",
            "stop the local jetty container (in case started in background process)",
            "",
            "",
            "mvn jetty:stop"
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
        }else if (args[0] == "help") {
            if (args[1])
                help(args[1])
            else
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
        } catch (Exception e) {
            e.printStackTrace()
        } finally {
            out.flush()
            out.close()
        }
    }

}

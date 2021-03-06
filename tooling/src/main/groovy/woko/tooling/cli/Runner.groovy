/*
 * Copyright 2001-2012 Remi Vankeisbelck
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

    private final Logger logger
    private final Map<String, Command> commands = [:]
    private final File workingDir = new File(System.getProperty("user.dir"))

    File getWorkingDir() {
        return workingDir
    }

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

    def help(String commandName) {
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
            // store lengths of the 1rst 2 cols in order to
            // pad this correctly
            int ln0 = 0, ln1 = 0
            commands.each { k, v ->
                ln0 = Math.max(ln0, k.length())
                ln1 = Math.max(ln1, v.argSpec ? v.argSpec.length() : 0)
//                log("  - $k $v.argSpec\t\t:\t\t$v.shortDesc")
            }
            ln0++
            ln1++
            commands.each { k, v ->
                log("  - ${k.padRight(ln0)} ${v.argSpec.padRight(ln1)} : $v.shortDesc")
            }
            log("  - ${'help'.padRight(ln0)} ${'[command_name]'.padRight(ln1)} : display help about specified command")
            log("")
        }
    }

    /**
     * Create a runner for passed args
     * @param logger the logger to be used
     * @param workingDir the working directory (should be the top level project folder)
     */
    Runner(Logger logger, File workingDir) {
        this.logger = logger
        this.workingDir = workingDir
        addCommands([
                new ListCmd(this),
                new CreateCmd(this),
                new CrudCmd(this),
                new PushCmd(this),
                new GenerateCmd(this),
                new StartCommand(this),
                new ProcessCmd(
                        this,
                        "stop",
                        "stop the local jetty container (in case started in background process)",
                        "",
                        "",
                        "mvn jetty:stop"
                ),
                new ProcessCmd(
                        this,
                        "build",
                        "rebuilds the whole application",
                        "",
                        "",
                        "mvn clean install"
                ),
                new EnvironmentsCmd(this),
                new ExecGroovyCmd(this)
        ])
    }

    def invokeCommand(String... args) {
        if (!args) {
            throw new IllegalArgumentException("0 args specified, we need at least the command name")
        }else if (args[0] == "help") {
            if (args.size()>1 && args[1])
                help(args[1])
            else
                help()
            
        } else {
            def command = commands[args[0]]
            if (!command) {
                log("ERROR: Command '${args[0]}' not found")
                help()
            } else {
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
    }

    /**
     * Run with passed arguments
     */
    void run(String... args) {
        if (!args) {
            args = ["help"]
        }
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

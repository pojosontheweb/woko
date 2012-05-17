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

package woko.tooling.cli.commands

import woko.tooling.cli.Command
import woko.tooling.cli.Runner
import woko.tooling.utils.Logger

class ProcessCmd extends Command {

    private final String commandLine

    ProcessCmd(Runner runner, String name, String shortDesc, String argSpec, String longHelp, String commandLine) {
        super(runner, name, shortDesc, argSpec, longHelp)
        this.commandLine = commandLine
    }

    @Override
    def execute(List<String> args) {
        new ProcessExec().execute(logger, commandLine, args, { line ->
            handleLine(line)
        }, { Process p ->
            logger.indentedLog(" Process terminated for $name, returned ${p.exitValue()}")
        })
    }

    protected void handleLine(String line) {
        logger.indentedLog(line)
    }

}

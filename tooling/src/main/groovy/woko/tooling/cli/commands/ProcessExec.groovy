/*
 * Copyright 2001-2012 Remi Vankeisbelck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package woko.tooling.cli.commands

import woko.tooling.utils.Logger

class ProcessExec {

    boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase()
		return (os.indexOf("win") >= 0)
    }

    String argsToStr(List<String> args) {
        StringBuilder sb = new StringBuilder()
        if (args) {
            args.each {
                sb.append(it).append(" ")
            }
        }
        return sb.toString()
    }

    def execute(Logger logger, String commandLine, List<String> args, Closure lineHandler, Closure finishHandler) {
        String cmdLineWithArgs = "$commandLine ${argsToStr(args)}"
        String cmd = windows ?  "cmd /c $cmdLineWithArgs" : cmdLineWithArgs
        logger.indentedLog(" Launching process : $cmd")
        Process p = cmd.execute()
        if (lineHandler) {
            p.in.eachLine { lineHandler(it) }
        }
        p.waitFor()
        if (finishHandler) {
            finishHandler(p)
        }
    }

}

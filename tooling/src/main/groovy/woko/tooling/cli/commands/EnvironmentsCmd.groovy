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

import woko.tooling.cli.Command
import woko.tooling.cli.Runner
import woko.tooling.utils.Logger

class EnvironmentsCmd extends Command {

    EnvironmentsCmd(Runner runner) {
        super(runner,
              "env",
              "manage the environments",
              "list|use <env_name>",
              """Manages the environments for your app. Environments can be found in
the 'environments' folder, at the project root. You can put any resources in the environments
folders and use this command in order to copy the resources from an environment into the
packaged application.""")
    }

    @Override
    def execute(List<String> args) {
        def arg0 = getArgAt(args, 0)
        switch(arg0) {
            case "list" :
                doList()
                break
            case "use" :
                doUse(args)
                break
            default :
                log("ERROR : argument required : list | use")
        }
    }

    File getEnvironmentsFolder() {
        return new File(projectDir.absolutePath + File.separator + "environments")
    }

    private void doList() {
        def folder = environmentsFolder
        if (!folder.exists()) {
            log("'environments' folder not found under project root. No environments defined.")
        } else {
            // list folders in 'environments'
            def envs = folder.listFiles({ f ->
                f.isDirectory()
            } as FileFilter)
            if (envs.size()>0) {
                iLog("${envs.size()} environment(s) found :")
                envs.each { f ->
                    iLog("  - $f.name")
                }
            } else {
                iLog("No environments found.")
            }
        }
    }

    private void doUse(args) {
        // invoke the maven plugin
        String envName = getArgAt(args, 1)
        if (!envName) {
            iLog("ERROR : Environment name must be specified")
        } else {
            File folder = new File(environmentsFolder.absolutePath + File.separator + envName)
            if (!folder.exists()) {
                iLog("ERROR : Environment not found : folder '$folder.absolutePath' doesn't exist !")
            } else {
                // all good ! invoke the maven process...
                new ProcessExec().execute(logger, "mvn process-resources -Dwoko.env=$envName", [], { String line ->
                    iLog(line)
                }, { Process p ->
                    iLog("You are now on environment '$envName'")
                })
            }
        }
    }


}

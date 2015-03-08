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

import static woko.tooling.utils.AppUtils.*
import woko.tooling.utils.AppHttpClient
import com.google.common.io.Files
import woko.tooling.utils.AppUtils

class PushCmd extends Command {

    PushCmd(Runner runner) {
        super(
          runner,
          "push",
          "pushes the local facets to a remote application",
          "[resources|quiet]",
          """Scans the local facets in your project and pushes them
to a running woko application. The url argument defaults to :

  http://localhost:8080/<app_name>

Once pushed, your local facets will be available in the running application
like if they had been created at init-time.

The aim of this command is to speed up development by avoiding useless
server restarts when you change facet code.
""")
    }

    String pkgToPath(String pkg) {
        pkg.replaceAll("\\.", "/")
    }

    @Override
    def execute(List<String> args) {
        String arg1 = getArgAt(args, 0)
        boolean resources = false
        boolean quiet = false
        if (arg1=="resources") {
            resources = true
        } else {
            quiet = arg1 == "quiet"

            String url = "http://localhost:8080/$pomHelper.model.build.finalName"
            String username = "wdevel"
            String password = "wdevel"
            if (!quiet) {
                url = askWithDefault("Application url", url)
                username = askWithDefault("Developer username", username)
                password = askWithDefault("          password", password)
            } else {
                iLog("quiet mode : pushing all !")
            }

            // scan the local facet sources
            def baseDir = "$projectDir.absolutePath/src/main/groovy"
            def facetPackages = []
            def userPkgs = computeUserFacetPackages()
            if (userPkgs) {
                facetPackages.addAll(userPkgs)
            }
            facetPackages << "facets"
            def facetSources = [:]
            facetPackages.each { facetPkg ->
                String pkgPath = pkgToPath(facetPkg)
                String basePkgDir = "$baseDir/$pkgPath"
                File basePkg = new File(basePkgDir)
                if (basePkg.exists()) {
                    basePkg.eachFileRecurse { File f ->
                        if (!f.isDirectory() && f.name.endsWith(".groovy")) {
                            String fileText = f.text
                            if (fileText =~ /@FacetKey/) {
                                // looks like a facet, add to sources
                                facetSources[f.path] = fileText
                            }
                        }
                    }
                }
            }

            if (facetSources) {
                iLog("The following facet source files will be pushed :")
                def httpParams = [:]
                int index = 0
                facetSources.each { k,v ->
                    iLog("  - $k")
                    httpParams["facet.sources[$index]"] = v
                    index++
                }
                if (quiet || yesNoAsk("Shall we push this")) {
                    // convert to woko-enabled params for the push facet

                    AppHttpClient c = new AppHttpClient(logger, url, AppUtils.isBuiltInAuth(pomHelper))
                    c.doWithLogin(username, password) {
                        c.post("/push", httpParams) { String resp ->
                            resp.eachLine { l ->
                                iLog(l)
                            }
                        }
                    }
                }
            } else {
                iLog("No facet sources found, nothing will be pushed")
            }
        }

        if (resources || quiet || yesNoAsk("Do you want to redeploy your web resources to /target")) {
            String targetDir = "$projectDir.absolutePath/target/${pomHelper.model.build.finalName}"
            File target = new File(targetDir)
            if (!target.exists()) {
                target.mkdirs()
            }
            String sourceDir = "$projectDir.absolutePath/src/main/webapp"
            File source = new File(sourceDir)
            if (!source.exists()) {
                log("webapp dir not found : $sourceDir")
            } else {
                def extensions = ["jsp", "html", "js", "css", "png", "jpg", "jpeg"]
                iLog("Copying from webapp with extensions $extensions")
                int nbCopied = 0;
                source.eachFileRecurse { File f ->
                    if (!f.isDirectory()) {
                        String name = f.name
                        boolean copy = false
                        for (String ext : extensions) {
                            if (name.endsWith(ext)) {
                                copy = true
                                break
                            }
                        }
                        if (copy) {
                            String relativePath = f.absolutePath[sourceDir.length()..-1]
                            String toPath = "$targetDir$relativePath"
                            File targetFile = new File(toPath)
                            if (!targetFile.exists()) {
                                new File(targetFile.parent).mkdirs()
                            }
                            try {
                                Files.copy(f, targetFile)
                                nbCopied++
                            } catch(Exception e) {
                                log("ERROR : exception while copying $f.absolutePath to $toPath")
                            }
                        }
                    }
                }
                iLog("  - $nbCopied resource(s) copied")
            }
        }
    }

}

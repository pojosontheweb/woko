package woko.tooling.cli.commands

import woko.tooling.cli.Command
import woko.tooling.cli.Runner
import woko.tooling.utils.Logger

import static woko.tooling.utils.AppUtils.*
import woko.tooling.utils.AppHttpClient

class PushCmd extends Command {

    PushCmd(Runner runner,File projectDir,Logger logger) {
        super(
          runner,
          projectDir,
          logger,
          "push",
          "pushes the local facets to a remote application",
          "[url]",
          """Scans the local facets in your project and pushes them
to a running woko application. The url argument defaults to :

  http://localhost:8080/<app_name>

One pushed, your local facets will be available in the running application
like if they had been created at init-time.

The aim of this command is to speed up development by avoiding useless
server restarts when you change facet code.
""")
    }

    String pkgToPath(String pkg) {
        pkg.replaceAll(/\./, File.separator)
    }

    @Override
    void execute(List<String> args) {
        String url = getArgAt(args, 0)
        if (!url) {
            url = "http://localhost:8080/$pomHelper.model.build.finalName"
        }
        iLog "Pushing to $url"
        String username = askWithDefault("Developer username", "wdevel")
        String password = askWithDefault("          password", "wdevel")

        // scan the local facet sources
        def baseDir = "$projectDir.absolutePath/src/main/groovy"
        def facetPackages = []
        facetPackages << computeUserFacetPackages()
        facetPackages << "facets"
        def facetSources = [:]
        facetPackages.each { facetPkg ->
            String basePkgDir = "$baseDir/${pkgToPath(facetPkg)}"
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
            if (yesNoAsk("Shall we push this")) {
                // convert to woko-enabled params for the push facet

                AppHttpClient c = new AppHttpClient(logger, url, pomHelper)
                c.doWithLogin(username, password) {
                    c.post("/push", httpParams) { String resp ->
                        log(resp)
                    }
                }
            }
        } else {
            log("No facet sources found, nothing will be pushed")
        }
    }

}

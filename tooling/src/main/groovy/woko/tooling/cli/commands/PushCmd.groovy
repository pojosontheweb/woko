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

    @Override
    void execute(List<String> args) {
        String url = getArgAt(args, 0)
        if (!url) {
            url = "http://localhost:8080/$pomHelper.model.build.finalName"
        }
        iLog "Pushing to $url"
        String username = askWithDefault("Developer username", "wdevel")
        String password = askWithDefault("          password", "wdevel")

        // scan the local facets and gather descriptors for facets in project
        def fdm = getFdm()
        def baseDir = "$projectDir.absolutePath/src/main"
        def facetPackages = []
        facetPackages << computeUserFacetPackages()
        facetPackages << "facets"
        def descriptorsToPush = []
        fdm.descriptors.each { fd ->
            def fqcn = fd.facetClass.name
            boolean isProjectFacet = false
            for (String facetPackage : facetPackages) {
                if (fqcn.startsWith(facetPackage)) {
                    isProjectFacet = true
                    break
                }
            }
            if (isProjectFacet) {
                descriptorsToPush << fd
            }
        }

        if (descriptorsToPush) {
            def facetSources = [:]
            descriptorsToPush.each { fd ->
                def fqcn = fd.facetClass.name
                def facetLocalPath = "$baseDir/groovy/${fqcn.replaceAll(/\./, "/")}.groovy"
                File facetSourceFile = new File(facetLocalPath)
                if (!facetSourceFile.exists()) {
                    iLog("WARNING : Facet source not found : $facetLocalPath - will not be pushed !")
                } else {
                    // file exists, add contents to facet sources
                    facetSources[facetLocalPath] = facetSourceFile.text
                }
            }
            if (facetSources) {
                iLog("The following facet source files will be pushed :")
                def httpParams = [:]
                int index = 0
                facetSources.each { k,v ->
                    iLog("  - $k")
                    httpParams["facet.sources[$index]", v]
                    index++
                }
                if (yesNoAsk("Shall we push this")) {
                    // convert to woko-enabled params for the push facet

                    AppHttpClient c = new AppHttpClient(url)
                    c.doWithLogin(username, password) {
                        c.post("/push", httpParams) { String resp ->
                            log("Push result :")
                            log(resp)
                        }
                    }
                }
            } else {
                log("No facet sources found, nothing will be pushed")
            }
        } else {
            log("No facets found in your project, nothing pushed. Please check your facet packages in web.xml :")
            facetPackages.each {
                log("  - $it")
            }

        }
    }


}

package woko.tooling.cli

import woko.tooling.utils.Logger
import woko.Woko
import woko.WokoInitListener
import net.sourceforge.jfacets.IFacetDescriptorManager
import woko.tooling.utils.PomHelper

abstract class Command {

    final String name
    final String shortDesc
    final String argSpec
    final String longHelp
    final Logger logger
    final File projectDir
    final Runner runner

    private def webXmlSlurped
    private PomHelper pomHelper

    Command(Runner runner, File projectDir, Logger logger, String name, String shortDesc, String argSpec, String longHelp) {
        this.projectDir = projectDir
        this.logger = logger
        this.name = name
        this.shortDesc = shortDesc
        this.argSpec = argSpec
        this.longHelp = longHelp
        this.runner = runner
    }

    protected String getArgAt(List<String> args, int i) {
        if (i>args.size()) {
            return null
        }
        if (i<0) {
            return null
        }
        return args[i]
    }

    protected def getWebXml() {
        if (!webXmlSlurped) {
            String pathToWebXml = "$projectDir.absolutePath/src/main/webapp/WEB-INF/web.xml"
            File webXmlFile = new File(pathToWebXml)
            if (!webXmlFile) {
                logger.error("Unable to locate the web.xml in your project ! Should be in $pathToWebXml")
                return null
            }
            webXmlSlurped = new XmlSlurper().parse(webXmlFile)
        }
        return webXmlSlurped
    }

    protected List<String> computeFacetPackages() {
        def packages = computeUserFacetPackages()
        packages.addAll(Woko.DEFAULT_FACET_PACKAGES);
        return packages
    }

    protected List<String> computeUserFacetPackages() {
        def packages = null
        // TODO UGLY : don't loop if you don't need to !
        webXml["context-param"].each { it ->
            if (it["param-name"].text() == "Woko.Facet.Packages") {
                String facetPackages = it["param-value"].text()
                packages = []
                packages.addAll(WokoInitListener.extractPackagesList(facetPackages))
            }
        }
        return packages
    }

    protected IFacetDescriptorManager getFdm() {
        Woko.createFacetDescriptorManager(computeFacetPackages())
    }

    protected void iLog(msg) {
        logger.indentedLog(msg)
    }

    protected void log(msg) {
        logger.log(msg)
    }

    protected PomHelper getPomHelper() {
        if (pomHelper==null) {
            File pomFile = new File("$projectDir.absolutePath/pom.xml")
            if (!pomFile) {
                logger.error("pom file not found in project dir $projectDir")
            }
            pomHelper = new PomHelper(pomFile)
        }
        return pomHelper
    }

    protected String getGroupId() {
        return pomHelper.model.groupId
    }

    abstract void execute(List<String> args)

}

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
import woko.Woko
import woko.WokoInitListener
import net.sourceforge.jfacets.IFacetDescriptorManager
import woko.tooling.utils.PomHelper
import org.apache.maven.model.Dependency
import woko.tooling.utils.AppUtils

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

    Command(Runner runner, String name, String shortDesc, String argSpec, String longHelp) {
        this.projectDir = runner.workingDir
        this.logger = runner.logger
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
        if (packages) {
            packages.addAll(Woko.DEFAULT_FACET_PACKAGES);
        } else {
            packages = Woko.DEFAULT_FACET_PACKAGES
        }
        return packages
    }

    protected List<String> computeUserFacetPackages() {
        def packages = []
        // TODO UGLY : don't loop if you don't need to !
        webXml["context-param"].each { it ->
            if (it["param-name"].text() == "Woko.Facet.Packages") {
                String facetPackages = it["param-value"].text()
                packages.addAll(WokoInitListener.extractPackagesList(facetPackages))
            }
        }
        return packages
    }

    protected List<String> computeModelPackages() {
        def packages = []
        // TODO UGLY : don't loop if you don't need to !
        webXml["context-param"].each { it ->
            if (it["param-name"].text() == "Woko.Hibernate.Packages") {
                String facetPackages = it["param-value"].text()
                packages.addAll(WokoInitListener.extractPackagesList(facetPackages))
            }
        }
        return packages
    }

    protected List<String> computeActionPackages() {
        def packages = []
        // TODO UGLY : don't loop if you don't need to !
        webXml.filter.each { filter->
            if (filter["filter-class"].text()=="net.sourceforge.stripes.controller.StripesFilter") {
                filter["init-param"].each { it ->
                    if (it["param-name"].text() == "ActionResolver.Packages") {
                        String facetPackages = it["param-value"].text()
                        packages.addAll(WokoInitListener.extractPackagesList(facetPackages))
                    }
                }
            }
        }
        return packages
    }

    protected IFacetDescriptorManager getFdm() {
        Woko.createFacetDescriptorManager(computeFacetPackages())
    }

    protected String makePath(String... parts) {
        StringBuilder sb = new StringBuilder()
        for (int i=0;i<parts.length;i++) {
            sb << parts[i]
            if (i<parts.length-1) {
                sb << File.separator
            }
        }
        return sb.toString()
    }

    protected IFacetDescriptorManager getFdmCustomClassLoader() {
        def urls = []
        [
                makePath(projectDir.absolutePath,"target","classes"),
                makePath(projectDir.absolutePath,"target",artifactId,"WEB-INF","classes")
        ].each {
            urls << new File(it).toURL()
        }
        File webInfLib = new File(makePath(projectDir.absolutePath,"target",artifactId,"WEB-INF","lib"))
        webInfLib.eachFile { File f ->
            if (f.name.endsWith(".jar")) {
                urls << f.toURL()
            }
        }
        URL[] ua = new URL[urls.size()]
        ua = (URL[])urls.toArray(ua)
        URLClassLoader classLoader = new URLClassLoader(ua, Runner.class.getClassLoader())
        Woko.createFacetDescriptorManager(computeFacetPackages(), classLoader)
    }

    protected void iLog(msg) {
        logger.indentedLog(msg)
    }

    protected void log(msg) {
        logger.log(msg)
    }

    protected PomHelper getPomHelper() {
        if (pomHelper==null) {
            pomHelper = AppUtils.getPomHelper(projectDir)
        }
        return pomHelper
    }

    protected String getGroupId() {
        return getPomHelper().model.groupId
    }

    protected String getArtifactId() {
        return getPomHelper().model.artifactId
    }

    protected String capitalize(String str) {
        if (!str) {
            return str
        }
        return str[0].toUpperCase() + str[1..-1]
    }

    protected String modelFqcnFromSimpleName(String simpleName) {
        if (simpleName.indexOf(".")==-1) {
            // infer the package...
            def pkgs = []
            pkgs.addAll(computeModelPackages())
            pkgs.addAll(["java.lang", "java.util"])
            for (String pkg : pkgs) {
                try {
                    String className = pkg + "." + simpleName
                    Class.forName(className)
                    simpleName = className
                    iLog("Resolved target type : $simpleName")
                    break
                } catch(ClassNotFoundException e) {
                    // let it go through : not the good class
                }
            }
        }
        return simpleName
    }

    protected boolean isGroovyAvailable() {
        try {
            Class.forName("groovy.lang.GroovyObject")
            return true
        } catch (Exception e) {
            return false
        }
    }

    protected boolean askIfUseGroovy(boolean quiet) {
        if (isGroovyAvailable()) {
            if (quiet) {
                iLog("Groovy is available in your project, facet will be written in Groovy")
                return true
            } else {
                iLog("Groovy seems to be available in your project...")
                iLog(" ") // line sep
                return AppUtils.yesNoAsk("Do you want to write the facet in Groovy")
            }
        }
        return false
    }

    protected String getBaseFacetPackage() {
        def basePackage = getGroupId()
        def userFacetPgkList = computeUserFacetPackages()
        if (userFacetPgkList!=null) {
            if (userFacetPgkList.size() > 1) {
                log('Several facets package found :')
                int i=0
                userFacetPgkList.each {
                    iLog("$i : $it")
                    i++
                }
                def facetsPkg = Integer.valueOf(AppUtils.askWithDefault("In which package would you like to create this facet ?", "0"))
                basePackage = userFacetPgkList.get(facetsPkg)
            } else {
                basePackage = userFacetPgkList.get(0)
            }
        }
        return basePackage
    }



    abstract def execute(List<String> args)

}

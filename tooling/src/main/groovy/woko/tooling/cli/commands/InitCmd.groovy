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

package woko.tooling.cli.commands

import groovy.text.GStringTemplateEngine
import org.apache.maven.model.Dependency
import woko.tooling.cli.Command
import woko.tooling.cli.Runner
import woko.tooling.utils.AppUtils
import woko.tooling.utils.Logger

class InitCmd extends Command{

    final Logger logger

    String packageName
    Boolean useBootstrap
    Boolean useGroovy
    String webApp
    String modelPath
    String wokoPath
    String facetsPath

    InitCmd(Runner runner, File projectDir, Logger logger) {
        super(
                runner,
                projectDir,
                logger,
                "init",
                "Initialize a new Woko project",
                "[-use-boostrap {yes|no}] [-use-groovy {yes|no}] [-default-package-name <package name>]",
                """Initialize a new Woko project""")

    }

    @Override
    void execute(List<String> args) {

        def cli = new CliBuilder(usage: 'woko init [-use-boostrap {yes|no}] [-use-groovy {yes|no}] [-default-package-name <package name>]')

        cli.with {
            h longOpt: 'help', 'Show usage information'
            b longOpt: 'use-boostrap', args: 1, argName: 'yes|no', 'boostrap usage'
            g longOpt: 'use-groovy', args: 1, argName: 'yes|no', 'groovy usage'
            p longOpt: 'default-package-name', args: 1,   'default package name'
        }

        String defaultPackageName = groupId
        if (!defaultPackageName.endsWith(artifactId)) {
            defaultPackageName = "$defaultPackageName.$artifactId"
        }

        def options = cli.parse(args.toArray())
        if (options) {
            if(options.h) {
                cli.usage()
                return
            }

            if(options.b)
            {
                useBootstrap = (options.b == "yes")
            } else {
                useBootstrap = AppUtils.yesNoAsk("Would you like to use Bootstrap for UI")
            }

            if(options.g)
            {
                useGroovy = (options.g == "yes")
            } else {
                useGroovy = AppUtils.yesNoAsk("Would you like to use Groovy")
            }

            if(options.p)
            {
                packageName = options.p
            } else {
                packageName = AppUtils.askWithDefault("Specify your default package name", defaultPackageName)
            }

        } else {
            useBootstrap = AppUtils.yesNoAsk("Would you like to use Bootstrap for UI")
            useGroovy = AppUtils.yesNoAsk("Would you like to use Groovy")
            packageName = AppUtils.askWithDefault("Specify your default package name", defaultPackageName)
        }


        addSpecificsDependencies()
        createPackage()
        createWebXml()
        createClass()
        copyResources()

        iLog("")
        iLog("Your project has been generated in : $artifactId.")
        iLog("Run 'woko start' in order to launch your app in a local Jetty container")
    }

    private void addSpecificsDependencies(){
        if (useBootstrap){
            // Add a dependency on bootstrap in pom
            Dependency bootStrapDep = new Dependency()
            bootStrapDep.groupId = "com.rvkb"
            bootStrapDep.artifactId = "woko-web-bootstrap"
            bootStrapDep.version = '${woko.version}'
            bootStrapDep.type = "war"
            pomHelper.addDependency(bootStrapDep)
        }else{
            // Add a dependency on Lithium in pom
            Dependency lithiumDep = new Dependency()
            lithiumDep.groupId = "com.rvkb"
            lithiumDep.artifactId = "woko-web-lithium"
            lithiumDep.version = '${woko.version}'
            lithiumDep.type = "war"
            pomHelper.addDependency(lithiumDep)
        }

        if (useGroovy){
            // Add a dependency on Groovy in pom
            Dependency groovyDep = new Dependency()
            groovyDep.groupId = "org.codehaus.groovy"
            groovyDep.artifactId = "groovy"
            groovyDep.version = "1.7.4"
            pomHelper.addDependency(groovyDep)
        }else{
            iLog("You will use pure Java")
        }
    }

    /**
     * Convention : package name = groupId.artifactId
     */
    private void createPackage(){
        String srcBasePath, testBasePath
        if (useGroovy){
            srcBasePath = 'src'+File.separator+'main'+File.separator+'groovy'
            testBasePath = 'src'+File.separator+'test'+File.separator+'groovy'
        }else{
            srcBasePath = 'src'+File.separator+'main'+File.separator+'java'
            testBasePath = 'src'+File.separator+'test'+File.separator+'java'
        }

        String srcPath = srcBasePath + File.separator+packageName.replaceAll("\\.", "\\"+File.separator)
        String testPath = testBasePath + File.separator+packageName.replaceAll("\\.", "\\"+File.separator)
        facetsPath = srcPath+File.separator+'facets'
        modelPath = srcPath+File.separator+'model'
        wokoPath = srcPath+File.separator+'woko'

        String srcResources = 'src'+File.separator+'main'+File.separator+'resources'
        String testResources = 'src'+File.separator+'test'+File.separator+'resources'
        webApp = 'src'+File.separator+'main'+File.separator+'webapp'+
                File.separator+'WEB-INF'

        if (!createDirectory(facetsPath)){
            logger.error('An error occurs during the facets source directory creation')
        }
        if (!createDirectory(modelPath)){
            logger.error('An error occurs during the model source directory creation')
        }
        if (!createDirectory(wokoPath)){
            logger.error('An error occurs during the woko source directory creation')
        }
        if (!createDirectory(testPath)){
            logger.error('An error occurs during the maven TEST directory creation')
        }
        if (!createDirectory(srcResources)){
            logger.error('An error occurs during the maven SRC RESOURCES directory creation')
        }
        if (!createDirectory(testResources)){
            logger.error('An error occurs during the maven TEST RESOURCES directory creation')
        }
        if (!createDirectory(webApp)){
            logger.error('An error occurs during the webapp directory creation')
        }
    }

    private void createWebXml(){

        def binding = [:]
        binding['name'] = artifactId
        binding['modelPackage'] = packageName+'.model'
        binding['facetsPackage'] = packageName+'.facets'

        FileWriter writer = new FileWriter(webApp+File.separator+'web.xml')
        generateTemplate(binding, 'web-xml', false, writer)

        // Summary
        iLog("- web.xml file created : " + webApp+File.separator+'web.xml')
    }

    private void createClass(){
        // Generate example POJO
        def bindingPOJO = [:]
        bindingPOJO['modelPackage'] = packageName+".model"

        FileWriter writer = new FileWriter(modelPath+File.separator+"MyEntity" + (useGroovy ? ".groovy" : ".java"))
        generateTemplate(bindingPOJO, 'my-entity', useGroovy, writer)

        // Generate default Layout facet
        def bindingFacets = [:]
        bindingFacets['facetsPackage'] = packageName+".facets"
        bindingFacets['name'] = artifactId

        writer = new FileWriter(facetsPath+File.separator+"MyLayout" + (useGroovy ? ".groovy" : ".java"))
        generateTemplate(bindingFacets, 'layout', useGroovy, writer)
        iLog("- Layout facet created : " + packageName+".facets.MyLayout")
    }

    private void copyResources(){
        FileWriter writer = new FileWriter('src'+File.separator+'main'+
                File.separator+'resources'+File.separator+'application.properties')
        generateTemplate(null, 'application', false, writer)
        iLog("- resource bundle created : " + artifactId +File.separator+'src'+File.separator+'main'+
                File.separator+'resources'+File.separator+'application.properties')
    }

    private boolean createDirectory(String path){
        File dir = new File(path)
        return dir.mkdirs()
    }

    private void generateTemplate(Map binding, String templateName, boolean useGroovy, Writer out){
        def engine = new GStringTemplateEngine()
        def tpl = templateName + (useGroovy ? "-groovy.template" : ".template")
        engine.createTemplate(this.class.getResource(tpl)).make(binding).writeTo(out)
        out.flush()
        out.close()
    }
}

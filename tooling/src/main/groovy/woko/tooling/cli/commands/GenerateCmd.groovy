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

import groovy.text.GStringTemplateEngine
import org.apache.maven.model.Dependency
import woko.tooling.cli.Command
import woko.tooling.cli.Runner
import woko.tooling.utils.AppUtils
import org.apache.maven.model.Plugin
import org.codehaus.plexus.util.xml.Xpp3Dom
import org.apache.maven.model.PluginExecution

class GenerateCmd extends Command{

    //final Logger logger

    String packageName
    Boolean useBootstrap
    Boolean useGroovy
    String webApp
    String modelPath
    String wokoPath
    String facetsPath
    Boolean useRegistration

    GenerateCmd(Runner runner) {
        super(
                runner,
                "generate",
                "Generates a new Woko project",
                "[-use-boostrap {yes|no}] [-use-groovy {yes|no}] [-default-package-name <package name>]",
                """Initialize a new Woko project""")

    }

    @Override
    def execute(List<String> args) {

        CliBuilder cliBuilder = new CliBuilder(usage: 'woko generate')

        cliBuilder.writer = new PrintWriter(logger.writer)

        cliBuilder.with {
            h longOpt: 'help', 'Show usage information'
            b longOpt: 'use-boostrap', args: 1, argName: 'yes|no', 'boostrap usage'
            g longOpt: 'use-groovy', args: 1, argName: 'yes|no', 'groovy usage'
            p longOpt: 'default-package-name', args: 1, argName: 'com.example.myapp', 'default package name'
            r longOpt: 'registration', args: 1, argName: 'yes|no', 'use registration'
        }

        String defaultPackageName = groupId

        if (!defaultPackageName.endsWith(artifactId)) {
            defaultPackageName = "$defaultPackageName.${artifactId.toLowerCase()}"
        }

        def options = cliBuilder.parse(args.toArray())
        if (!options) {
            options = new OptionAccessor()
        }

        if(options.h) {
            cliBuilder.usage()
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

        if (options.r)
        {
            useRegistration =(options.r=='yes')
        } else {
            useRegistration = AppUtils.yesNoAsk("Do you want usermanagement and registration")
        }

        addSpecificsDependencies()
        createPackage()
        createWebXml()
        createInitListener()
        createClass()
        copyResources()

        iLog("")
        iLog("Your project has been generated in : $projectDir.name ")
        iLog("Run 'woko start' in order to launch your app in a local Jetty container")
    }

    private void addSpecificsDependencies(){
        if (useBootstrap){
            Dependency bootStrapDep = new Dependency()
            bootStrapDep.groupId = "com.pojosontheweb"
            bootStrapDep.artifactId = "woko-bootstrap-core"
            bootStrapDep.version = '${woko.version}'
            pomHelper.addDependency(bootStrapDep, false)
            bootStrapDep = new Dependency()
            bootStrapDep.groupId = "com.pojosontheweb"
            bootStrapDep.artifactId = "woko-web-bootstrap"
            bootStrapDep.version = '${woko.version}'
            bootStrapDep.type = "war"
            pomHelper.addDependency(bootStrapDep, true)
        }else{
            // Add a dependency on Lithium in pom
            Dependency lithiumDep = new Dependency()
            lithiumDep.groupId = "com.pojosontheweb"
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
            groovyDep.version = "2.0.4"
            pomHelper.addDependency(groovyDep)

            // Add the GMAVEN plugin
            Plugin gmaven = new Plugin()
            // Create plugin description
            gmaven.groupId = "org.codehaus.gmaven"
            gmaven.artifactId = "gmaven-plugin"
            gmaven.version = "1.4"
            // Add plugin configuration
            Xpp3Dom configNode = new Xpp3Dom('configuration')
            Xpp3Dom providerSelectionNode = new Xpp3Dom('providerSelection')
            providerSelectionNode.value = '1.8'
            configNode.addChild(providerSelectionNode)
            gmaven.configuration = configNode
            // Add execution
            PluginExecution execution = new PluginExecution()
            execution.addGoal('generateStubs')
            execution.addGoal('compile')
            execution.addGoal('generateTestStubs')
            execution.addGoal('testCompile')
            gmaven.addExecution(execution)

            pomHelper.addPlugin(gmaven)

        }else{
            iLog("You will use pure Java")
        }
        if (useRegistration) {
            // dependency on user management war
            pomHelper.addDependency(new Dependency(
                    groupId:"com.pojosontheweb",
                    artifactId: "woko-usermanagement-web",
                    version: '${woko.version}', // NOT a Gstring !!
                    type: "war"
            ))
        }
    }

    /**
     * Convention : package name = groupId.artifactId
     */
    private void createPackage(){
        String srcBasePath, testBasePath
        if (useGroovy){
            srcBasePath = projectDir.absolutePath + File.separator + 'src'+File.separator+'main'+File.separator+'groovy'
            testBasePath = projectDir.absolutePath + File.separator + 'src'+File.separator+'test'+File.separator+'groovy'
        }else{
            srcBasePath = projectDir.absolutePath + File.separator + 'src'+File.separator+'main'+File.separator+'java'
            testBasePath = projectDir.absolutePath + File.separator + 'src'+File.separator+'test'+File.separator+'java'
        }

        String srcPath = srcBasePath + File.separator+packageName.replaceAll("\\.", "\\"+File.separator)
        String testPath = testBasePath + File.separator+packageName.replaceAll("\\.", "\\"+File.separator)
        facetsPath = srcPath+File.separator+'facets'
        modelPath = srcPath+File.separator+'model'
        wokoPath = srcPath+File.separator+'woko'

        String srcResources = projectDir.absolutePath + File.separator + 'src'+File.separator+'main'+File.separator+'resources'
        String testResources = projectDir.absolutePath + File.separator + 'src'+File.separator+'test'+File.separator+'resources'
        webApp = projectDir.absolutePath + File.separator + 'src'+File.separator+'main'+File.separator+'webapp'+
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

        StringBuilder facetPackages = new StringBuilder("${packageName}.facets")
        if (useRegistration) {
            facetPackages << " woko.ext.usermanagement.facets"
        }

        FileWriter writer = new FileWriter(webApp+File.separator+'web.xml')
        generateTemplate([
                name: artifactId,
                modelPackage: packageName+'.model',
                facetsPackage: facetPackages.toString(),
                packageName: packageName
        ], 'web-xml', false, writer)

        // Summary
        iLog("- web.xml file created : " + 'src'+File.separator+'main'+ File.separator+'webapp'+File.separator+
                'WEB-INF'+File.separator+'web.xml')
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

        String templateName = useBootstrap ? 'layout-bootstrap' : 'layout'
//        if (useBootstrap) {

//            bindingFacets['css'] = '''
//        String baseBootstrap = "/css/bootstrap-v2.3.0/bootstrap.css";
//
//        if (getRequest() != null){
//            String theme = (String)getRequest().getSession().getAttribute(SwithThemeActionBean.THEME_COOKIE);
//            if (theme != null) {
//                baseBootstrap = "/css/" + theme + "/bootstrap.css";
//            }
//        }
//
//        return Arrays.asList(baseBootstrap, "/css/responsive.css","/css/woko.css");
//'''
//        } else {
//            bindingFacets['css'] = '''
//        return Arrays.asList("/woko/css/layout-all.css", "/woko/css/lithium/assets/style.css");
//'''
//        }

        generateTemplate(bindingFacets, templateName, useGroovy, writer)
        iLog("- Layout facet created : " + packageName+".facets.MyLayout")
    }

    private void createInitListener() {
        String initListenerClassName = "${artifactId.capitalize()}InitListener"

        String fileName = "$wokoPath$File.separator${initListenerClassName}"
        if (useGroovy) {
            fileName += ".groovy"
        } else {
            fileName += ".java"
        }

        File f = new File(fileName)
        f.withWriter { w ->
            generateTemplate(
                    [
                        name:artifactId,
                        packageName: "${packageName}.woko",
                        className: initListenerClassName
                    ],
                    'init-listener'
                    , useGroovy,
                    w)
        }
    }

    private void copyResources(){
        FileWriter writer = new FileWriter(projectDir.absolutePath + File.separator + 'src'+File.separator+'main'+
                File.separator+'resources'+File.separator+'application.properties')
        generateTemplate(null, 'application', false, writer)
        iLog("- resource bundle created : " + 'src'+File.separator+'main'+ File.separator+'resources'+File.separator+
                'application.properties')
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

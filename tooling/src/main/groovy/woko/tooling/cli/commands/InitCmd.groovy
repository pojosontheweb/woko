package woko.tooling.cli.commands

import woko.tooling.cli.Command
import woko.tooling.cli.Runner
import woko.tooling.utils.Logger
import woko.tooling.utils.AppUtils
import groovy.text.GStringTemplateEngine
import groovy.text.Template
import org.apache.maven.model.Dependency


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
                "",
                """Initialize a new Woko project""")
    }

    @Override
    void execute(List<String> args) {
        addSpecificsDependencies()
        createPackage()
        createWebXml()
        createClass()
        copyResources()

        iLog(" ") // line sep
        iLog(" --- Summary ---")
        iLog(" ") // line sep
        iLog("Your project has been generated in : $artifactId folder, and contains : ")
        iLog("- Some dependencies have been added to your pom")
        iLog("- A web.xml file has been created")
        iLog("- Default POJO has been added in your model package")
        iLog("- The application name has been overridden through a Layout facet")
        iLog("- A application.properties has been added")
        iLog(" ") // line sep
        iLog("Use 'woko start' command to launch your app in a Jetty embedded server")
    }

    private void addSpecificsDependencies(){
        useBootstrap = AppUtils.yesNoAsk("Would you like to use Bootstrap for UI")
        if (useBootstrap){
            // Add a dependency on bootstrap in pom
            Dependency bootStrapDep = new Dependency()
            bootStrapDep.groupId = "com.rvkb"
            bootStrapDep.artifactId = "woko-web-bootstrap"
            bootStrapDep.version = '${woko.version}'
            bootStrapDep.type = "war"
            pomHelper.addDependency(bootStrapDep)
            iLog("A dependency on woko-web-bootstrap has been added to your pom")
        }else{
            // Add a dependency on Lithium in pom
            Dependency lithiumDep = new Dependency()
            lithiumDep.groupId = "com.rvkb"
            lithiumDep.artifactId = "woko-web-lithium"
            lithiumDep.version = '${woko.version}'
            bootStrapDep.type = "war"
            pomHelper.addDependency(lithiumDep)
            iLog("A dependency on woko-web-lithium has been added to your pom")
        }

        useGroovy = AppUtils.yesNoAsk("Would you like to use Groovy language")
        if (useGroovy){
            // Add a dependency on Groovy in pom
            Dependency groovyDep = new Dependency()
            groovyDep.groupId = "org.codehaus.groovy"
            groovyDep.artifactId = "groovy"
            groovyDep.version = "1.7.4"
            pomHelper.addDependency(groovyDep)
            iLog("A dependency on groovy has been added to your pom")
        }else{
            iLog("You will use pure Java")
        }
        log("") // jump line
    }

    /**
     * Convention : package name = groupId.artifactId
     */
    private void createPackage(){
        iLog("We will generate all needed directories...")
        String srcBasePath, testBasePath
        if (useGroovy){
            srcBasePath = 'src'+File.separator+'main'+File.separator+'groovy'
            testBasePath = 'src'+File.separator+'test'+File.separator+'groovy'
        }else{
            srcBasePath = 'src'+File.separator+'main'+File.separator+'java'
            testBasePath = 'src'+File.separator+'test'+File.separator+'java'
        }

        packageName = AppUtils.askWithDefault("Specify your default package name", groupId+"."+artifactId)
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
        iLog("...All directories have been created")
        log("") // jump line

    }

    private void createWebXml(){
        iLog("Generation of the web.xml file")
        // Ask for 'push' command
        Boolean pushCmd = AppUtils.yesNoAsk("Would you like enable the woko 'push' command");
        String initListenerClassName = "woko.ri.RiWokoInitListener"

        if (pushCmd){
            initListenerClassName = generateInitListener()
        }


        def binding = [:]
        binding['name'] = artifactId
        binding['modelPackage'] = packageName+'.model'
        binding['facetsPackage'] = packageName+'.facets'
        binding['initListenerClassName'] = initListenerClassName

        FileWriter writer = new FileWriter(webApp+File.separator+'web.xml')
        generateTemplate(binding, 'web-xml', false, writer)

        // Summary
        iLog("web.xml file generation :")
        iLog("- A web.xml file has been created : " + webApp+File.separator+'web.xml')
        if (pushCmd){
            iLog("- A new InitListener has been generated in : " + packageName+'.woko.'+initListenerClassName)
            iLog("- A new dependency on woko-push has been added to your pom")
        }
        log('') //jump line
    }

    private String generateInitListener(){
        iLog("To enable the woko 'push' command, we will generate a default WokoInitListener")
        String initListenerClassName = AppUtils.askWithDefault("How you want to name this listener ?", "MyInitListener")

        // Generate the Listener
        def binding = [:]
        binding['wokoPackage'] = packageName+'.woko'
        binding['className'] = initListenerClassName
        FileWriter writer = new FileWriter(wokoPath+File.separator+initListenerClassName+ (useGroovy ? ".groovy" : ".java") )
        generateTemplate(binding, 'initListener', useGroovy, writer)

        // Add dependency on woko-push in pom
        Dependency pushDep = new Dependency()
        pushDep.groupId = "com.rvkb"
        pushDep.artifactId = "woko-push"
        pushDep.version = '${woko.version}'
        pomHelper.addDependency(pushDep)

        return packageName+'.woko.'+initListenerClassName
    }

    private void createClass(){
        iLog("Now we will generated a default POJO as example")
        // Generate example POJO
        def bindingPOJO = [:]
        bindingPOJO['modelPackage'] = packageName+".model"

        FileWriter writer = new FileWriter(modelPath+File.separator+"MyEntity" + (useGroovy ? ".groovy" : ".java"))
        generateTemplate(bindingPOJO, 'my-entity', useGroovy, writer)
        iLog("MyEntity domain model had been created : " + packageName+".model.MyEntity")
        log("")  // jump line

        iLog("A default Layout facet will be created to specify your application name")
        // Generate default Layout facet
        def bindingFacets = [:]
        bindingFacets['facetsPackage'] = packageName+".facets"
        bindingFacets['name'] = artifactId

        writer = new FileWriter(facetsPath+File.separator+"MyLayout" + (useGroovy ? ".groovy" : ".java"))
        generateTemplate(bindingFacets, 'layout', useGroovy, writer)
        iLog("MyLayout facet has been created : " + packageName+".facets.MyLayout")
        log("")  // jump line
    }

    private void copyResources(){
        iLog("Generation of the resources")
        FileWriter writer = new FileWriter('src'+File.separator+'main'+
                File.separator+'resources'+File.separator+'application.properties')
        generateTemplate(null, 'application', false, writer)
        iLog("application.resource has been created in : " + name+File.separator+'src'+File.separator+'main'+
                File.separator+'resources'+File.separator+'application.properties')
        log("")  // jump line
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

package woko.tooling.cli.commands

import woko.tooling.cli.Command
import woko.tooling.cli.Runner
import woko.tooling.utils.Logger
import woko.tooling.utils.AppUtils
import groovy.text.GStringTemplateEngine
import groovy.text.Template


class InitCmd {

    final Logger logger

    String name
    String description
    String groupId
    String artifactId
    Boolean bootstrap
    Boolean groovy
    String webApp
    String modelPath
    String wokoPath
    String facetsPath

    InitCmd(Logger logger) {
        this.logger = logger
    }

    @Override
    void execute() {
        init()
        createPom()
        createPackage()
        createWebXml()
        createClass()
        copyResources()
    }

    private void init(){
        // Create project folder
        logger.log("# Global project information")
        logger.log("----------------------------")
        name = AppUtils.requiredAsk('Project name');
        description = AppUtils.simpleAsk('Project Description');
        if (!createDirectory(name)){
            logger.error('An error occurs during the project folder creation')
            System.exit(1)
        }
        logger.log("") // jump line
    }

    private void createPom(){
        // Create pom
        logger.log("# Generate pom.xml for maven")
        logger.log("----------------------------")
        groupId = AppUtils.askWithDefault('Maven groupId ?', "com."+name.toLowerCase());
        artifactId = AppUtils.askWithDefault('Maven artifactId ?', name.toLowerCase());
        String versionId = AppUtils.askWithDefault("Your application's version ?", "0.1-SNAPSHOT");
        String wokoVersion = AppUtils.askWithDefault('Which version of Woko you want to use ?', "2.0-SNAPSHOT");
        bootstrap = AppUtils.yesNoAsk('Would you like to use Bootstrap UI');
        groovy = AppUtils.yesNoAsk('Would you like to use Groovy language');

        private final def binding = [:]
        binding['name'] = name
        binding['description'] = description
        binding['artifactId'] =artifactId
        binding['groupId'] = groupId
        binding['version'] = versionId
        binding['wokoVersion'] = wokoVersion
        binding['bootstrap'] = bootstrap
        binding['groovy'] = groovy

        FileWriter writer = new FileWriter(name+File.separator+'pom.xml')
        generateTemplate(binding, 'pom', false, writer)
        logger.indentedLog("pom.xml file generated")
        logger.log("") // jump line
    }

    /**
     * Convention : package name = groupId.artifactId
     */
    private void createPackage(){
        logger.log("# Generate folders and packages")
        logger.log("-------------------------------")
        String srcBasePath, testBasePath
        if (groovy){
            srcBasePath = name+File.separator+'src'+File.separator+'main'+File.separator+'groovy'
            testBasePath = name+File.separator+'src'+File.separator+'test'+File.separator+'groovy'
        }else{
            srcBasePath = name+File.separator+'src'+File.separator+'main'+File.separator+'java'
            testBasePath = name+File.separator+'src'+File.separator+'test'+File.separator+'java'
        }

        String srcPath = srcBasePath + File.separator+groupId.replaceAll("\\.", "\\"+File.separator)+File.separator+artifactId
        String testPath = testBasePath + File.separator+groupId.replaceAll("\\.", "\\"+File.separator)+File.separator+artifactId
        facetsPath = srcPath+File.separator+'facets'
        modelPath = srcPath+File.separator+'model'
        wokoPath = srcPath+File.separator+'woko'

        String srcResources = name+File.separator+'src'+File.separator+'main'+File.separator+'resources'
        String testResources = name+File.separator+'src'+File.separator+'test'+File.separator+'resources'
        webApp = name+File.separator+'src'+File.separator+'main'+File.separator+'webapp'+
                File.separator+'WEB-INF'

        if (!createDirectory(facetsPath)){
            logger.error('An error occurs during the facets source directory creation')
            System.exit(1)
        }
        if (!createDirectory(modelPath)){
            logger.error('An error occurs during the model source directory creation')
            System.exit(1)
        }
        if (!createDirectory(wokoPath)){
            logger.error('An error occurs during the woko source directory creation')
            System.exit(1)
        }
        if (!createDirectory(testPath)){
            logger.error('An error occurs during the maven TEST directory creation')
            System.exit(1)
        }
        if (!createDirectory(srcResources)){
            logger.error('An error occurs during the maven SRC RESOURCES directory creation')
            System.exit(1)
        }
        if (!createDirectory(testResources)){
            logger.error('An error occurs during the maven TEST RESOURCES directory creation')
            System.exit(1)
        }
        if (!createDirectory(webApp)){
            logger.error('An error occurs during the webapp directory creation')
            System.exit(1)
        }
        logger.indentedLog("All your folders have been created")
        logger.log("") // jump line

    }

    private void createWebXml(){
        logger.log("# Generate the web.xml")
        logger.log("----------------------")
        // Ask for 'push' command
        Boolean pushCmd = AppUtils.yesNoAsk("Would you like enable the woko 'push' command");
        String initListenerClassName = "woko.ri.RiWokoInitListener"

        if (pushCmd){
            // Generate the InitListener
            initListenerClassName = generateInitListener()
        }


        def binding = [:]
        binding['name'] = name
        binding['description'] = description
        binding['modelPackage'] = groupId+'.'+artifactId+'.model'
        binding['facetsPackage'] = groupId+'.'+artifactId+'.facets'
        binding['initListenerClassName'] = initListenerClassName

        FileWriter writer = new FileWriter(webApp+File.separator+'web.xml')
        generateTemplate(binding, 'web-xml', false, writer)

        logger.indentedLog("web.xml file created")
        if (pushCmd)
            logger.indentedLog("As you want to enjoy the 'push' command a Listener has been created too")
        
        logger.log('') //jump line
    }

    private String generateInitListener(){
        logger.log("To enable the woko 'push' command, we will generate a default WokoInitListener")
        String initListenerClassName = AppUtils.askWithDefault("How you want to named this listener ?", "MyInitListener")

        def binding = [:]
        binding['wokoPackage'] = groupId+'.'+artifactId+'.woko'
        binding['className'] = initListenerClassName

        FileWriter writer = new FileWriter(wokoPath+File.separator+initListenerClassName+ (groovy ? ".groovy" : ".java") )
        generateTemplate(binding, 'initListener', groovy, writer)

        return groupId+'.'+artifactId+'.woko.'+initListenerClassName
    }

    private void createClass(){
        logger.log("# Generate default model")
        logger.log("------------------------")
        // Generate example POJO
        def bindingPOJO = [:]
        bindingPOJO['modelPackage'] = groupId+"."+artifactId+".model"

        FileWriter writer = new FileWriter(modelPath+File.separator+"MyEntity" + (groovy ? ".groovy" : ".java"))
        generateTemplate(bindingPOJO, 'my-entity', groovy, writer)
        logger.indentedLog("MyEntity domain model had been created")
        logger.log("")  // jump line


        logger.log("# Generate Layout facet for All roles")
        logger.log("-------------------------------------")
        // Generate default Layout facet
        def bindingFacets = [:]
        bindingFacets['facetsPackage'] = groupId+"."+artifactId+".facets"
        bindingFacets['name'] = name

        writer = new FileWriter(facetsPath+File.separator+"MyLayout" + (groovy ? ".groovy" : ".java"))
        generateTemplate(bindingFacets, 'layout', groovy, writer)
        logger.indentedLog("MyLayout facet has been created")
        logger.log("")  // jump line
    }

    private void copyResources(){
        logger.log("# Generate default application resources")
        logger.log("----------------------------------------")
        FileWriter writer = new FileWriter(name+File.separator+'src'+File.separator+'main'+
                File.separator+'resources'+File.separator+'application.properties')
        generateTemplate(null, 'application', false, writer)
        logger.indentedLog("application.resource has been created")
        logger.log("")  // jump line
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

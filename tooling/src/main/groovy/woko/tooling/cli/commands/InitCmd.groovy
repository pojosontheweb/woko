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

    InitCmd(Logger logger) {
        this.logger = logger
    }

    @Override
    void execute() {
        init()
        createPom()
        createPackage()
        createWebXml()
//        createJavaClass()
//        copyResources()
    }

    private void init(){
        // Create project folder
        name = AppUtils.requiredAsk('Project name : ');
        description = AppUtils.simpleAsk('Project Description : ');
        if (!createDirectory(name)){
            logger.error('An error occurs during the project folder creation')
            System.exit(1)
        }
    }

    private void createPom(){
        // Ask some questions
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
    }

    /**
     * Convention : package name = groupId.artifactId
     */
    private void createPackage(){
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
        String facetPath = srcPath+File.separator+'facets'
        modelPath = srcPath+File.separator+'model'
        String wokoPath = srcPath+File.separator+'woko'

        String srcResources = name+File.separator+'src'+File.separator+'main'+File.separator+'resources'
        String testResources = name+File.separator+'src'+File.separator+'test'+File.separator+'resources'
        webApp = name+File.separator+'src'+File.separator+'main'+File.separator+'webapp'+
                File.separator+'WEB-INF'

        if (!createDirectory(facetPath)){
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

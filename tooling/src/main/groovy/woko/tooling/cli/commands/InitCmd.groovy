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



    private void generateTemplate(Map binding, String templateName, boolean useGroovy, Writer out){
        def engine = new GStringTemplateEngine()
        def tpl = templateName + (useGroovy ? "-groovy.template" : ".template")
        engine.createTemplate(this.class.getResource(tpl)).make(binding).writeTo(out)
        out.flush()
        out.close()
    }
}

package woko.tooling

import com.rvkb.woko.tools.utils.AppUtils
import com.rvkb.woko.tools.utils.Log

class ProjectBuilder {

    Log log
    String name
    String description

    String groupId
    String artifactId
    Boolean bootstrap
    Boolean groovy
    String webApp
    String modelPath

    ProjectBuilder(Log log) {
        this.log = log
    }

    public void build(){
        init()
        createPom()
        createPackage()
        createWebXml()
        createJavaClass()
        copyResources()
    }

    private void init(){
        // Create project folder
        name = AppUtils.requiredAsk('Project name : ');
        description = AppUtils.simpleAsk('Project Description : ');
        if (!createDirectory(name)){
            log.error('An error occurs during the project folder creation')
            System.exit(1)
        }
    }

    private void createPom(){
        // Ask some questions
        groupId = AppUtils.askWithDefault('Maven groupId ?', "com."+name.toLowerCase());
        artifactId = AppUtils.askWithDefault('Maven artifactId ?', name.toLowerCase());
        String wokoVersion = AppUtils.askWithDefault('Woko version ?', "2.0-beta2-SNAPSHOT");
        bootstrap = AppUtils.yesNoAsk('Would you like to use Bootstrap UI ?');
        groovy = AppUtils.yesNoAsk('Would you like to use Groovy language ?');

        def props = [
                'name':name,
                'artifactId':artifactId,
                'groupId':groupId,
                'wokoVersion':wokoVersion,
                'bootstrap':bootstrap,
                'groovy':groovy
        ]

        FileWriter writer = new FileWriter(name+File.separator+'pom.xml')
        AppUtils.generateTemplate(props, 'pom', writer)
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
            log.error('An error occurs during the facets source directory creation')
            System.exit(1)
        }
        if (!createDirectory(modelPath)){
            log.error('An error occurs during the model source directory creation')
            System.exit(1)
        }
        if (!createDirectory(wokoPath)){
            log.error('An error occurs during the woko source directory creation')
            System.exit(1)
        }
        if (!createDirectory(testPath)){
            log.error('An error occurs during the maven TEST directory creation')
            System.exit(1)
        }
        if (!createDirectory(srcResources)){
            log.error('An error occurs during the maven SRC RESOURCES directory creation')
            System.exit(1)
        }
        if (!createDirectory(testResources)){
            log.error('An error occurs during the maven TEST RESOURCES directory creation')
            System.exit(1)
        }
        if (!createDirectory(webApp)){
            log.error('An error occurs during the webapp directory creation')
            System.exit(1)
        }
    }

    private void createWebXml(){
        def props = [
                'name':name,
                'description': description,
                'modelPackage': groupId+'.'+artifactId+'.model',
                'facetsPackage': groupId+'.'+artifactId+'.facets',
        ]

        FileWriter writer = new FileWriter(webApp+File.separator+'web.xml')
        AppUtils.generateTemplate(props, 'web-xml', writer)
    }

    private void createJavaClass(){
        def props = [
                'modelPackage': groupId+'.'+artifactId+'.model',
        ]

        FileWriter writer = new FileWriter(modelPath+File.separator+'MyEntity.java')
        AppUtils.generateTemplate(props, 'my-entity', writer)
    }

    private void copyResources(){
        def props = [
                'bootstrap': bootstrap,
        ]

        FileWriter writer = new FileWriter(name+File.separator+'src'+File.separator+'main'+
                File.separator+'resources'+File.separator+'WokoResources.properties')
        AppUtils.generateTemplate(props, 'WokoResources', writer)
    }

    private boolean createDirectory(String path){
        File dir = new File(path)
        return dir.mkdirs()
    }
}

package woko.tooling.cli

import woko.tooling.utils.Logger
import woko.Woko
import woko.WokoInitListener
import net.sourceforge.jfacets.FacetDescriptor
import net.sourceforge.jfacets.IFacetDescriptorManager
import static woko.tooling.utils.AppUtils.*
import woko.facets.builtin.WokoFacets
import woko.facets.FragmentFacet
import woko.tooling.utils.PomHelper

class Runner {

    Logger logger

    // the commands map : String -> Command !
    def commands = [:]

    File workingDir = new File(System.getProperty("user.dir"))

    Runner addCommand(String name, String shortDesc, String argSpec, Closure c) {
        def lh = LongHelpMessages.MSGS[name]
        if (!lh) {
            lh = "no long help available"
        }
        commands[name] = new Command(name, shortDesc, argSpec, lh, c)
        return this
    }

    private def getWebXml() {
        String pathToWebXml = "$workingDir.absolutePath/src/main/webapp/WEB-INF/web.xml"
        File webXml = new File(pathToWebXml)
        if (!webXml) {
            logger.error("Unable to locate the web.xml in your project ! Should be in $pathToWebXml")
            return null
        }
        new XmlSlurper().parse(webXml)
    }

    private List<String> computeFacetPackages(webXml) {
        def packages = computeUserFacetPackages(webXml)
        packages.addAll(Woko.DEFAULT_FACET_PACKAGES);
        return packages
    }

    private List<String> computeUserFacetPackages(webXml) {
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

    private IFacetDescriptorManager getFdm() {
        Woko.createFacetDescriptorManager(computeFacetPackages(webXml))
    }

    private void iLog(msg) {
        logger.indentedLog(msg)
    }

    private void log(msg) {
        logger.log(msg)
    }

    Runner() {
        addCommand("help", "display help about specified command", "[command_name]") { p1 -> // first arg could be the name of the command
            if (p1) {
                def c = commands[p1]
                if (c) {
                    log("Help for command '$p1' : $c.shortDesc")
                    log("\nUsage :\n")
                    log(" - woko $p1 $c.argSpec")
                    if (c.longHelp) {
                        log("\n$c.longHelp")
                    }
                } else {
                    log("No such command : $p1")
                }
            } else {
                log("Usage : woko <command> arg*")
                log("\nAvailable commands :\n")
                commands.each { k, v ->
                    log("  - $k $v.argSpec\t\t:\t\t$v.shortDesc")
                }
            }
        }.
                addCommand("list", "list facets or roles", "facets|roles") {
                    p1 ->
                    switch (p1) {
                        case "facets":
                            def fdm = getFdm()
                            def descriptors = fdm.descriptors
                            log("${descriptors.size()} facets found : ")
                            descriptors.sort { a,b ->
                                a.name <=> b.name
                            }.each { FacetDescriptor d ->
                                println "  $d.name, $d.profileId, $d.targetObjectType.name, $d.facetClass.name"
                            }
                            break
                        case "roles":
                            def fdm = getFdm()
                            def descriptors = fdm.descriptors
                            def allRoles = []
                            descriptors.each { FacetDescriptor d ->
                                def role = d.profileId
                                if (!allRoles.contains(role)) {
                                    allRoles << role
                                }
                            }
                            log("${allRoles.size()} role(s) used in faced keys :")
                            allRoles.sort().each { r ->
                                println "  $r"
                            }
                            break
                        default:
                            logger.error("invalid list command")
                            invokeCommand(["help","list"])
                            break
                    }
                }.
                addCommand("create", "create project elements", "facet|entity") { p1 ->
                    switch (p1) {
                        case "facet" :
                            def fdm = getFdm()

                            def genFiles = []

                            // ask for required params and assist the best we can !

                            def name = requiredAsk("Facet name")
                            // check if there are facets with this name already
                            def facetsWithSameName = fdm.descriptors.findAll { fd -> fd.name == name }
                            if (facetsWithSameName) {
                                // facet override !
                                iLog("Found ${facetsWithSameName.size()} facet(s) with the same name :")
                                facetsWithSameName.each { fd ->
                                    iLog("  - $fd.name, $fd.profileId, $fd.targetObjectType.name, $fd.facetClass")
                                }
                                iLog("You might be overriding a facet...")
                            } else {
                                iLog("No facet(s) with that name already, you are not overriding anything...")
                            }
                            iLog(" ") // line sep

                            def role = askWithDefault("Role", "all")
                            // check if the facet already exists for that name and role
                            def facetsWithSameNameAndSameRole = facetsWithSameName.findAll { fd -> fd.profileId == role}
                            if (facetsWithSameNameAndSameRole) {
                                // facet overide !
                                iLog("Found ${facetsWithSameNameAndSameRole.size()} facet(s) with the same name and role :")
                                facetsWithSameNameAndSameRole.each { fd ->
                                    iLog("  - $fd.name, $fd.profileId, $fd.targetObjectType.name, $fd.facetClass")
                                }
                                iLog("You are overriding an existing facet by type...")
                            } else {
                                if (role=="all") {
                                    iLog("You are assigning the facet to all users of the application...")
                                }
                            }
                            iLog(" ") // line sep

                            def targetType = askWithDefault("Target type", "java.lang.Object")

                            // check if a facet exists for the same type
                            def identicalFacets = facetsWithSameNameAndSameRole.findAll { fd -> fd.targetObjectType.name == targetType }
                            if (identicalFacets) {
                                iLog("Found ${identicalFacets.size()} facet(s) with the exact same key :")
                                identicalFacets.each { fd ->
                                    iLog("  - $fd.facetClass")
                                }
                                iLog("You are REPLACING a facet. Make sure the ordering of the facet packages is ok : ")
                                computeFacetPackages(webXml).each {
                                    iLog("  - $it")
                                }
                                iLog(" ") // line sep
                            } else {
                                if (facetsWithSameName) {
                                    iLog("You are overriding facet(s) by type...")
                                    iLog(" ") // line sep
                                }
                            }

                            // now that we have the params, propose an appropriate base class if any
                            def baseClass = WokoFacets.getDefaultImpl(name)
                            def baseIntf = WokoFacets.getInterface(name)
                            if (baseClass && baseIntf) {
                                iLog("Found interface and possible base class for your facet :")
                                iLog("  - interface  : $baseIntf.name")
                                iLog("  - base class : $baseClass.name")
                            } else if (baseClass) {
                                iLog("Found possible base class your facet : $baseClass.name")
                            } else if (baseIntf) {
                                iLog("Found interface for your facet : $baseIntf.name")
                            }
                            if (baseClass) {
                                iLog(" ") // line sep
                                if (yesNoAsk("Do you want to use the base class")) {
                                    iLog("Your facet will extend $baseClass.name")
                                }
                            } else if (baseIntf) {
                                iLog("Your facet will implement $baseIntf.name")
                            }
                            iLog(" ") // line sep

                            // check if the facet is a fragment facet and propose JSP fragment if any
                            def targetObjectSimpleType = extractPkgAndClazz(targetType).clazz
                            def fragmentPath = null
                            if (baseIntf && FragmentFacet.isAssignableFrom(baseIntf)) {
                                iLog("The facet is a Fragment Facet...")
                                iLog(" ") // line sep
                                if (yesNoAsk("Do you want to generate JSP a fragment")) {
                                    fragmentPath = askWithDefault("Enter the JSP fragment path", "/WEB-INF/jsp/$role/${name}${targetObjectSimpleType}.jsp")
                                } else {
                                    fragmentPath = null
                                }
                            }
                            // check if there's a base class, and an associated JSP fragment
                            // by convention JSP fragments are stored as statics of the facet class
                            def baseClassFragmentPath = null
                            if (baseClass!=null) {
                                try {
                                    baseClassFragmentPath = baseClass.FRAGMENT_PATH
                                } catch(Exception) {
                                    iLog("No JSP fragment available from the base class, an empty JSP will be generated.")
                                    iLog(" ") // line sep
                                }
                            }

                            // grab maven pom
                            File pomFile = new File(workingDir.absolutePath + File.separator + "pom.xml")
                            def pm = new PomHelper(pomFile)


                            // ask for facet class name with default computed name
                            def basePackage = pm.model.groupId

                            def userFacetPgkList = computeUserFacetPackages(webXml)
                            if (userFacetPgkList.size() > 1) {
                                log('Several facets package found :')
                                int i=1
                                userFacetPgkList.each {
                                    iLog("$i : $it")
                                    i++
                                }
                                def facetsPkg = Integer.valueOf(askWithDefault("In which package would you like to create this facet ?", "1"))
                                basePackage = userFacetPgkList.get(facetsPkg-1)
                            }else
                                basePackage = userFacetPgkList.get(0)

                            if (!basePackage){
                                logger.error("unable to find your facet package definition. Please check your web.xml file.")
                                basePackage = simpleAsk("facets pakage ?")
                            }


                            def capName = name[0].toUpperCase() + name[1..-1]
                            def facetClassName = askWithDefault("Specify the facet class name",
                                    "${basePackage}.${role}.${capName}${targetObjectSimpleType}Impl")

                            // do we generate a Groovy or a Java class ?
                            // check if we have Groovy available in the project
                            // TODO better check !
                            def useGroovy = false
                            def groovyAvailable = new File("src/main/groovy").exists()
                            if (groovyAvailable) {
                                iLog("Groovy seems to be available in your project...")
                                iLog(" ") // line sep
                                useGroovy = yesNoAsk("Do you want to write the facet in Groovy")
                            }

                            // show summary of all infos
                            iLog(" ") // line sep
                            iLog(" --- Summary ---")
                            iLog(" ") // line sep
                            iLog(" Facet key         : $name, $role, $targetType")
                            iLog(" Facet class       : $facetClassName")
                            if (baseIntf) {
                                iLog(" Implements        : ${baseIntf.name}")
                            }
                            if (baseClass) {
                                iLog(" Extends           : ${baseClass.name}")
                            }
                            if (fragmentPath) {
                                iLog(" JSP fragment path : $fragmentPath")
                            }
                            def lang = useGroovy ? "Groovy" : "pure Java"
                            iLog(" Facet written in  : $lang")
                                    iLog(" Facet source dir  : $workingDir.absolutePath/src/main/${useGroovy ? "groovy" : "java"}")
                                    iLog(" ") // line sep


                            boolean doIt = yesNoAsk("Is this OK ? Shall we generate all this (n to view gen sources only)")

                            // file generation
                            def facetFilePath = new FacetCodeGenerator(logger,workingDir,name,role,facetClassName).
                                    setTargetObjectType(targetType).
                                    setBaseClass(baseClass).
                                    setInterface(baseIntf).
                                    setFragmentPath(fragmentPath).
                                    setUseGroovy(useGroovy).
                                    setDontGenerate(
                                            !doIt
                                    ).
                                    generate()
                            if (facetFilePath) {
                                genFiles << facetFilePath
                            }

                            // create the JSP fragment file if requested
                            if (fragmentPath) {

                                def genDefaultFragment = { w ->
                                    w << "Default generated JSP fragment for facet \${$name}..."
                                    iLog("Default JSP fragment generated.")
                                }

                                def output = logger.writer
                                if (doIt) {
                                    int i = fragmentPath.lastIndexOf("/")
                                    def pathToJsp = fragmentPath
                                    if (i!=-1) {
                                        pathToJsp = fragmentPath[0..i-1]
                                    }
                                    pathToJsp = "$workingDir/src/main/webapp/$pathToJsp"
                                    new File(pathToJsp).mkdirs()

                                    def genFragmentFullPath = "$workingDir/src/main/webapp/$fragmentPath"
                                    output = new FileOutputStream(genFragmentFullPath)
                                    genFiles << genFragmentFullPath
                                }

                                if (baseClassFragmentPath) {
                                    iLog("Using JSP fragment from base class : ")
                                    iLog("  - $baseClassFragmentPath")
                                    // look in target folder
                                    def finalName = pm.model.build.finalName
                                    String path = "$workingDir.absolutePath/target/$finalName$baseClassFragmentPath"
                                    def f = new File(path)
                                    if (!f.exists()) {
                                        iLog("File $path not found : build the project first ?" ) // TODO better error handling
                                        genDefaultFragment(output)
                                    } else {
                                        f.withReader { r -> output << r }
                                        output.flush()
                                        iLog("JSP fragment written")
                                    }
                                } else {
                                    genDefaultFragment(output)
                                }
                            }

                            log("${genFiles.size()} file(s) generated")
                            genFiles.each {
                                log("  - $it")
                            }

                            break
                        default:
                            logger.error("create $p1 is not implemented")
                                    invokeCommand(["help","create"])
                    }
                }.
                addCommand("run", "run the application in a local tomcat container", "") {
                    "mvn package cargo:start".execute()
                    log("Application sarted : http://localhost:8080/<app_name>")
                    log("woko stop to terminate the server")
                }.
                addCommand("stop", "stop the local tomcat container", "") {
                    "mvn cargo:stop".execute()
                    log("Application stopped")
                }
    }

    void invokeCommand(args) {
        def command = commands[args[0]]
        if (!command) {
            throw new IllegalArgumentException("command ${args[0]} does not exist")
        }
        def callback = command.callback
        if (!callback) {
            throw new IllegalStateException("command $command doesn't have no callback !")
        }
        def nbArgs = args.size()
        // switch because of closure param passing policy
        switch (nbArgs) {
            case 1:
                callback()
                break
            case 2:
                callback(args[1])
                break
            default:
                callback(args[1..-1])
        }

    }

    /**
     * Run with passed arguments
     * @param args an array or list of Strings containing the command line parameters
     */
    void run(args) {
        // Display home msg
        logger.splashMsg()
        args = args ? args : ["help"]
        invokeCommand(args)
    }

    public static void main(String[] args) {
        System.out.withWriter { w ->
            new Runner(logger: new Logger(w)).run(args)
        }
    }

}

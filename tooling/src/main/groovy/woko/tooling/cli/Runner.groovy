package woko.tooling.cli

import woko.tooling.utils.Logger
import woko.Woko
import woko.WokoInitListener
import net.sourceforge.jfacets.FacetDescriptor
import net.sourceforge.jfacets.IFacetDescriptorManager
import static woko.tooling.utils.AppUtils.*
import woko.facets.builtin.WokoFacets

class Runner {

    Logger logger

    // the commands map : String -> Command !
    def commands = [:]

    Runner addCommand(String name, String shortDesc, String argSpec, Closure c) {
        def lh = LongHelpMessages.MSGS[name]
        if (!lh) {
            lh = "no long help available"
        }
        commands[name] = new Command(name, shortDesc, argSpec, lh, c)
        return this
    }

    private def getWebXml() {
        String pathToWebXml = "./src/main/webapp/WEB-INF/web.xml"
        File webXml = new File(pathToWebXml)
        if (!webXml) {
            logger.error("Unable to locate the web.xml in your project ! Should be in $pathToWebXml")
            return null
        }
        new XmlSlurper().parse(webXml)
    }

    private def computeFacetPackages(webXml) {
        def packages = null
        // TODO UGLY : don't loop if you don't need to !
        webXml["context-param"].each { it ->
            if (it["param-name"].text() == "Woko.Facet.Packages") {
                String facetPackages = it["param-value"].text()
                packages = []
                packages.addAll(WokoInitListener.extractPackagesList(facetPackages))
                packages.addAll(Woko.DEFAULT_FACET_PACKAGES);
            }
        }
        return packages
    }

    private IFacetDescriptorManager getFdm() {
        Woko.createFacetDescriptorManager(computeFacetPackages(webXml))
    }

    private void indentedLog(msg) {
        logger.log("|  $msg")
    }

    Runner() {
        addCommand("help", "display help about specified command", "[command_name]") { p1 -> // first arg could be the name of the command
            if (p1) {
                def c = commands[p1]
                if (c) {
                    logger.log("Help for command '$p1' : $c.shortDesc")
                    logger.log("\nUsage :\n")
                    logger.log(" - woko $p1 $c.argSpec")
                    if (c.longHelp) {
                        logger.log("\n$c.longHelp")
                    }
                } else {
                    logger.log("No such command : $p1")
                }
            } else {
                logger.log("Usage : woko <command> arg*")
                logger.log("\nAvailable commands :\n")
                commands.each { k, v ->
                    logger.log("  - $k $v.argSpec\t\t:\t\t$v.shortDesc")
                }
            }
        }.
        addCommand("list", "list facets or roles", "facets|roles") {
          p1 ->
          switch (p1) {
              case "facets":
                  def fdm = getFdm()
                  def descriptors = fdm.descriptors
                  logger.log("${descriptors.size()} facets found : ")
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
                  logger.log("${allRoles.size()} role(s) used in faced keys :")
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

                  // ask for required params and assist the best we can !

                  def name = requiredAsk("Facet name")
                  // check if there are facets with this name already
                  def facetsWithSameName = fdm.descriptors.findAll { fd -> fd.name == name }
                  if (facetsWithSameName) {
                      // facet override !
                      indentedLog("Found ${facetsWithSameName.size()} facet(s) with the same name :")
                      facetsWithSameName.each { fd ->
                        indentedLog("  - $fd.name, $fd.profileId, $fd.targetObjectType.name, $fd.facetClass")
                      }
                      indentedLog("You might be overriding a facet...")
                  } else {
                      indentedLog("No facet(s) with that name already, you are not overriding anything...")
                  }
                  indentedLog(" ") // line sep

                  def role = askWithDefault("Role", "all")
                  // check if the facet already exists for that name and role
                  def facetsWithSameNameAndSameRole = facetsWithSameName.findAll { fd -> fd.profileId == role}
                  if (facetsWithSameNameAndSameRole) {
                      // facet overide !
                      indentedLog("Found ${facetsWithSameNameAndSameRole.size()} facet(s) with the same name and role :")
                      facetsWithSameNameAndSameRole.each { fd ->
                          indentedLog("  - $fd.name, $fd.profileId, $fd.targetObjectType.name, $fd.facetClass")
                      }
                      indentedLog("You are overriding an existing facet by type...")
                  } else {
                      if (role=="all") {
                          indentedLog("You are assigning the facet to all users of the application...")
                      }
                  }
                  indentedLog(" ") // line sep

                  def targetType = askWithDefault("Target type", "java.lang.Object")

                  // check if a facet exists for the same type
                  def identicalFacets = facetsWithSameNameAndSameRole.findAll { fd -> fd.targetObjectType.name == targetType }
                  if (identicalFacets) {
                      indentedLog("Found ${identicalFacets.size()} facet(s) with the exact same key :")
                      identicalFacets.each { fd ->
                          indentedLog("  - $fd.facetClass")
                      }
                      indentedLog("You are REPLACING a facet. Make sure the ordering of the facet packages is ok : ")
                      computeFacetPackages(webXml).each {
                          indentedLog("  - $it")
                      }
                  } else {
                      indentedLog("You are overriding facet(s) by type...")
                  }
                  indentedLog(" ") // line sep

                  // now that we have the params, propose an appropriate base class if any
                  def baseClass = WokoFacets.getDefaultImpl(name)
                  def baseIntf = WokoFacets.getInterface(name)
                  def useBaseClass = false
                  if (baseClass && baseIntf) {
                      indentedLog("Found interface and possible base class for your facet :")
                      indentedLog("  - interface  : $baseIntf.name")
                      indentedLog("  - base class : $baseClass.name")
                  } else if (baseClass) {
                      indentedLog("Found possible base class your facet : $baseClass.name")
                  } else if (baseIntf) {
                      indentedLog("Found interface for your facet : $baseIntf.name")
                  }
                  if (baseClass) {
                      def useBaseClassQuestion = askWithDefault("Do you want to use the base class ?", "y")
                      if (useBaseClassQuestion.toLowerCase()=="y") {
                        useBaseClass = true
                      }
                  }
                  if (baseIntf && !useBaseClass) {
                      indentedLog("Your facet will implement $baseIntf.name")
                  }
                  indentedLog(" ") // line sep

                  // check if the facet is a fragment facet and propose JSP fragment if any






                  break
              default:
                  logger.error("create is not implemented")
                  invokeCommand(["help","create"])
          }
        }.
        addCommand("run", "run the application in a local tomcat container", "") {
            "mvn package cargo:start".execute()
            logger.log("Application sarted : http://localhost:8080/<app_name>")
            logger.log("woko stop to terminate the server")
        }.
        addCommand("stop", "stop the local tomcat container", "") {
          "mvn cargo:stop".execute()
          logger.log("Application stopped")
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

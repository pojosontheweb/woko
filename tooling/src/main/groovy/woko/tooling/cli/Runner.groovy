package woko.tooling.cli

import woko.tooling.utils.Logger
import woko.Woko
import woko.WokoInitListener
import net.sourceforge.jfacets.FacetDescriptor

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

    Runner() {
        addCommand("help", "display help about specified command", "[command_name]") { p1 -> // first arg could be the name of the command
            if (p1) {
                def c = commands[p1]
                if (c) {
                    logger.log("Help for command '$p1' : $c.shortDesc")
                    logger.log("\nUsage :")
                    logger.log(" - woko $p1 $c.argSpec")
                    if (c.longHelp) {
                        logger.log("\n$c.longHelp")
                    }
                } else {
                    logger.log("No such command : $p1")
                }
            } else {
                logger.log("Usage : woko <command> arg*")
                logger.log("\nAvailable commands :")
                commands.each { k, v ->
                    logger.log("\n  - $k $v.argSpec")
                    logger.log("     $v.shortDesc")
                }
            }
        }.
        addCommand("list", "list facets or roles", "facets|roles") {
          p1 ->
          switch (p1) {
              case "facets":
                  logger.log("Facets in your project :")
                  File webXml = new File("./src/main/webapp/WEB-INF/web.xml")
                  if (!webXml) {
                      logger.error("Unable to locate the web.xml in your project !")
                  } else {
                      def wx = new XmlSlurper().parse(webXml)
                      wx["context-param"].each { it ->
                          if (it["param-name"].text() == "Woko.Facet.Packages") {
                              String facetPackages = it["param-value"].text()
                              def packages = []
                              packages.addAll(Woko.DEFAULT_FACET_PACKAGES);
                              packages.addAll(WokoInitListener.extractPackagesList(facetPackages))
                              def fdm = Woko.createFacetDescriptorManager(packages)
                              def descriptors = fdm.descriptors
                              logger.log("${descriptors.size()} descriptors found : ")
                              fdm.getDescriptors().each { FacetDescriptor d ->
                                  println "  $d.name, $d.profileId, $d.targetObjectType.name, $d.facetClass.name"
                              }
                          }
                      }
                  }
                  break
              case "roles":
                  logger.log("roles listing")
                  break
              default:
                  logger.error("invalid list command")
                  invokeCommand(["help","list"])
                  break
          }
        }.
        addCommand("create", "create project elements", "facet") {
          p1 ->
          switch (p1) {
              default:
                  logger.error("invalid create command : Only 'create project' is supported yet")
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

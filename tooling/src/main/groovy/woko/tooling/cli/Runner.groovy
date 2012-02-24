package woko.tooling.cli

import woko.tooling.ProjectBuilder
import woko.tooling.utils.Logger

class Runner {

    Logger logger

    // the commands map : String -> Command !
    def commands = [:]

    Runner addCommand(String name, String shortDesc, String argSpec, Closure c) {
        commands[name] = new Command(name, shortDesc, argSpec, c)
        return this
    }

    Runner() {
        addCommand("help", "display help about specified command", "[command_name]") { p1 -> // first arg could be the name of the command
            if (p1) {
                logger.log("Help for command '$p1' : ")
                def c = commands[p1]
                if (c) {
                    logger.log("  * Description :\t$c.shortDesc")
                    logger.log("  * Usage :\t\t\twoko $p1 $c.argSpec")
                }
            } else {
                logger.log("  * Usage : woko <command> arg*")
                logger.log("  * Available commands :")
                commands.each { k, v ->
                    logger.log("    - $k")
                }
            }
        }.
        addCommand("list", "list facets or roles", "facets|roles") {
          p1 ->
          switch (p1) {
              case "facets":
                  logger.log("facet listing")
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
        addCommand("create", "create project elements", "project|facet") {
          p1 ->
          switch (p1) {
              case "project":
                  ProjectBuilder pb = new ProjectBuilder(logger)
                  pb.build()
                  break
              default:
                  logger.error("invalid create command : Only 'create project' is supported yet")
                  invokeCommand(["help","create"])
          }
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

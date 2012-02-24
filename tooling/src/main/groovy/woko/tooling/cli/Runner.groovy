package woko.tooling.cli

import woko.tooling.ProjectBuilder
import woko.tooling.utils.Logger

class Runner {

    Logger logger

    // the commands map : String -> Closure !
    def commands = [
      "help": { p1 -> // first arg could be the name of the command
          if (p1) {
              logger.log("help about $p1")
          } else {
              logger.usage()
          }
      },
      "list": { p1 ->
          switch (p1) {
              case "facets" :
                  logger.log("facet listing")
                  break
              case "roles" :
                  logger.log("roles listing")
                  break
              default:
                  logger.error("invalid list command")
                  commands["help"]("list")
                  break
          }
      },
      "create": { p1 ->
          switch (p1) {
              case "project" :
                  ProjectBuilder pb = new ProjectBuilder(logger)
                  pb.build()
                  break
              default :
                  logger.error("Only 'create project' is supported yet")
          }
      }
    ]

    /**
     * Run with passed arguments
     * @param args an array or list of Strings containing the command line parameters
     */
    void run(args) {
        // Display home msg
        logger.splashMsg()

        // first arg is the name of the method to call
        if (!args) {
            commands["help"]()
        }
        def cmdName = args[0]
        def command = commands[cmdName]
        if (!command) {
            error("Command $cmdName does not exist !")
        } else {
            def nbArgs = args.size()
            // switch because of closure param passing policy
            switch (nbArgs) {
                case 1 :
                    command()
                    break
                case 2 :
                    command(args[1])
                    break
                default:
                    command(args[1..-1])
            }
        }
    }

    public static void main(String[] args) {
        System.out.withWriter { w->
            new Runner(logger: new Logger(w)).run(args)
        }
    }

}

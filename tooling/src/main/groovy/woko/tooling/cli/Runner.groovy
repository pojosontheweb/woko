package woko.tooling.cli

import woko.tooling.utils.Log
import woko.tooling.ProjectBuilder

class Runner {

    // output goes here
    Writer out

    private static Log log = new Log(true) // TODO remove


    // the commands map : String -> Closure !
    def commands = [
      "help": { p1 -> // first arg could be the name of the command
          if (p1) {
              log("help about $p1")
          } else {
              log("general help")
          }
      },
      "list": { p1 ->
          switch (p1) {
              case "facets" :
                  log("facet listing")
                  break
              case "roles" :
                  log("roles listing")
                  break
              default:
                  error("invalid list command")
                  commands["help"]("list")
                  break
          }
      },
      "create": { p1 ->
          switch (p1) {
              case "project" :
                  ProjectBuilder pb = new ProjectBuilder(log)
                  pb.build()
                  break
              default :
                  error("Only 'create project' is supported yet")
          }
      }
    ]

    /**
     * Run with passed arguments
     * @param args an array or list of Strings containing the command line parameters
     */
    void run(args) {
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

    void log(msg) {
        out ? out << "$msg\n" : println(msg)
    }

    void error(msg) {
        log("ERROR : $msg")
    }

    public static void main(String[] args) {
        System.out.withWriter { w->
            new Runner([out: w]).run(args)
        }
    }

}

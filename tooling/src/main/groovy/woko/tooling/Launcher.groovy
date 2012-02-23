package woko.tooling

import woko.tooling.utils.Log

/**
 * @author Alexis Boissonnat - alexis.boissonnat 'at' gmail.com
 */
class Launcher {

    private static Log log

    Launcher(Log log) {
        this.log = log
    }



    public static void main(String[] args){
        if (!log)
            log = new Log(false)

        // Display Welcome message
        println"""
__       __     _  __
\\ \\  _  / /___ | |/ / ___
 \\ \\/ \\/ // o \\|   K /   \\
  \\__W__/ \\___/|_|\\_\\\\_o_/  2.0
             POJOs on the Web !

"""

        // Check Arguments
        if (!args) {
            // No arguments -> Error
            log.usage('woko: missing argument file')
        }else {

            boolean byDefault = false

            for (int i=0 ; i<args.length ; i++){
                String a = args[i]
                if (a.equals("-h") || (a.equals("--help"))) {
                    log.usage()

                }else if(a.equals("create")){
                    if (args.length > i+1){
                        i++
                        String creationType = args[i]
                        if (creationType.equals('project')){
                            // Args are good to create project !
                            ProjectBuilder pb = new ProjectBuilder(log)
                            pb.build()
                        }else{
                            log.usage('woko: unkown creation type')
                        }
                    }else{
                        log.usage('woko: you need to specify what you want to create')
                    }
                }else{
                    log.usage('woko: bad arguments')
                }
            }
        }
    }

    private static void usage(String error){

    }
}
package woko.tooling.utils


class Logger {

    Writer writer

    Logger(Writer writer) {
        this.writer = writer
    }

    void log(String msg){
        writer ? writer<<"$msg\n" : println(msg)
    }

    void error(String error){
        log("ERROR : $error\n")
        usage()
    }

    void usage(){
        def msg = """Usage :
woko --help : Display help
woko create project <project_name> : Create a new Woko project
               """
        log(msg)
    }

    void splashMsg(){
        def msg = """
__       __     _  __
\\ \\  _  / /___ | |/ / ___
 \\ \\/ \\/ // o \\|   K /   \\
  \\__W__/ \\___/|_|\\_\\\\_o_/  2.0
             POJOs on the Web !

"""
        log(msg)
    }
}

package woko.tooling.utils


class Logger {

    Writer writer

    Logger(Writer writer) {
        this.writer = writer
    }

    void log(String msg){
        writer ? writer<<"$msg\n" : println(msg)
        writer.flush()
    }

    void error(String error){
        log("ERROR : $error\n")
    }

    void splashMsg(){
        def msg = """__       __     _  __
\\ \\  _  / /___ | |/ / ___
 \\ \\/ \\/ // o \\|   K /   \\
  \\__W__/ \\___/|_|\\_\\\\_o_/  2.0
             POJOs on the Web !"""
        log(msg)
    }
}

package woko.tooling.utils


class Log {
    
    String content

    boolean debugMode

    Log(boolean debugMode) {
        this.debugMode = debugMode
    }

    void printLog(String content){
        println(content)
        if (debugMode)
            this.content = content
    }

    void usage(String error){
        if (!error)
            error=''
        printLog """$error
Usage :
    woko --help : Display help
    woko create project <project_name> : Create a new Woko project
"""
    }

    void error(String error){
        printLog """woko: $error"""
    }
}

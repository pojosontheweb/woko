package woko.tooling.cli

class Command {

    String name
    String shortDesc
    String argSpec
    String longHelp
    Closure callback

    Command(String name, String shortDesc, String argSpec, String longHelp, Closure callback) {
        this.name = name
        this.shortDesc = shortDesc
        this.argSpec = argSpec
        this.callback = callback
        this.longHelp = longHelp
    }
}

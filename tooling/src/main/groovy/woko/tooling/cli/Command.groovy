package woko.tooling.cli

class Command {

    String name
    String shortDesc
    String argSpec
    Closure callback

    Command(String name, String shortDesc, String argSpec, Closure callback) {
        this.name = name
        this.shortDesc = shortDesc
        this.argSpec = argSpec
        this.callback = callback
    }
}

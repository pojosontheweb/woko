#!/usr/bin/env groovy

if (args.length<2) {
    println "Usage :"
    println "./bump-version.groovy oldVersion newVersion"
    return
}

def oldVersion = args[0]
def newVersion = args[1]

// handle all POMs
println "Handling all pom.xml files..."
int nbPoms = 0
new File(".").eachFileRecurse { File f ->
    if (!f.directory && !f.absolutePath.contains("target") && f.name == "pom.xml") {
        // replace version in pom
        String pomText = f.text
        int nbReplaces = 0
        while (pomText.contains(oldVersion)) {
            f.text = pomText.replaceFirst(oldVersion,newVersion)
            nbReplaces++
        }
        assert "invalid number of replacements in $f.absolutePath", nbReplaces == 1
        nbPoms++
    }
}

assert nbPoms == 36
println "Replaced versions in $nbPoms pom files"

println "Handling bash scripts..."
["woko", "woko.bat"].each {
    File f = new File("./tooling/src/main/scripts/$it")
    f.text = f.text.replace("-DarchetypeVersion=$oldVersion", "-DarchetypeVersion=$newVersion")
}
println "Done. Version bumped from $oldVersion to $newVersion"

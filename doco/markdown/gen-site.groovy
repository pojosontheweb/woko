#!/usr/bin/env groovy

@Grapes(
    @Grab(group='commons-io', module='commons-io', version='1.3.2')
)

String scriptDir = new File(getClass().protectionDomain.codeSource.location.path).parent

// create target dir
println "Creating target directory"
def targetDir = new File(scriptDir + File.separator + "target")

println "Copying web resources"
FileUtils.copyDirectory(
    new File(scriptDir + File.separator + "css"),
    targetDir
)

new File(scriptDir).listFiles().each { File f ->
    if (f.name.endsWith(".md")) {
        println f.name



    }
}

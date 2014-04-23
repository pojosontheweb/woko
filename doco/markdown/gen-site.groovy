#!/usr/bin/env groovy

@Grapes(
    @Grab(group='commons-io', module='commons-io', version='1.3.2')
)

import org.apache.commons.io.FileUtils

String scriptDir = new File(getClass().protectionDomain.codeSource.location.path).parent

// create target dir
println "Creating target directory"
def targetDir = new File(scriptDir + File.separator + "target")
if (targetDir.exists()) {
    FileUtils.deleteDirectory(targetDir)
}

println "Copying web resources"
FileUtils.copyDirectory(
    new File(scriptDir + File.separator + "webresources"),
    new File(targetDir.absolutePath)
)

println "Processing sources..."
new File(scriptDir + File.separator + "src").listFiles().each { File f ->
    if (f.name.endsWith(".md")) {
        println "=> Processing $f.absolutePath"
        println f.name
    } else {
        println "=> Copying file $f.absolutePath to target"
        FileUtils.copyFile(f, new File(targetDir.absolutePath + File.separator + f.name))
    }
}

println "Done."

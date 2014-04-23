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
println "Copying images"
def srcDir = scriptDir + File.separator + "src"
FileUtils.copyDirectory(
    new File(srcDir + File.separator + "img"),
    new File(targetDir.absolutePath + File.separator + "img")
)

String htmlTopNav = new File(srcDir + File.separator + "top-nav.include.html").text
def topNavXml = new XmlParser().parseText(htmlTopNav)

println "Processing sources..."
new File(srcDir).listFiles().each { File f ->
    if (!f.isDirectory()) {
        if (f.name.endsWith(".md")) {
            println "=> Processing $f.absolutePath"
            String nameWithoutExtension = f.name.substring(0, f.name.lastIndexOf("."))

            // patch top nav text in order to select active elem
            topNavXml.ul.li.each {
                def page_id = it.'@page-id'
                if (page_id==nameWithoutExtension) {
                    it.'@class' = 'active'
                }
            }
            StringWriter sw = new StringWriter()
            new XmlNodePrinter(
                new PrintWriter(sw)
            ).print(topNavXml)
            sw.close()
            def patchedTopNav = sw.toString()

            // create file with replaced top-nav
            String fileText = f.text
            String updatedText = fileText.replaceAll(/\{\{top-nav\.html}}/, htmlTopNav) // patchedTopNav
            println "patched : " + (f.absolutePath + ".patched.md")
            File patchedFile = new File(f.absolutePath + ".patched.md")
            try {
                patchedFile.text = updatedText
                def command = "./doc2html.sh $nameWithoutExtension"
                println "Command : $command"
                def p = command.execute()
                ByteArrayOutputStream out = new ByteArrayOutputStream()
                ByteArrayOutputStream err = new ByteArrayOutputStream()
                p.consumeProcessOutput(out, err)
                p.waitFor()
                def outS = out.toString()
                def res = outS ? outS : ""
                def errS = err.toString()
                res = errS ? res + errS : res
                def exitVal = p.exitValue()
                if (exitVal!=0) {
                    throw new RuntimeException("Process exit code : $exitVal. Output : $res")
                }
            } finally {
                patchedFile.delete()
            }
        } else if (!f.name.endsWith(".include.html")) {
            println "=> Copying file $f.absolutePath to target"
            FileUtils.copyFile(f, new File(targetDir.absolutePath + File.separator + f.name))
        }
    }
}

println "Done."

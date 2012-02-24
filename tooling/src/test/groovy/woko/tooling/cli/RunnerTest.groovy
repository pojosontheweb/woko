package woko.tooling.cli

import org.junit.Test
import org.junit.Ignore

class RunnerTest {

    def execCommand(args) {
        StringWriter sw = new StringWriter()
        new Runner([out:sw]).run(args)
        sw.flush()
        sw.close()
        sw.toString()
    }

    @Test
    void testCli() {
        [
          ["list"],
          ["list","facets"],
          ["list","roles"],
          ["help"]
        ].each { args ->
            println "*** executing command with args $args"
            def s = execCommand(args)
            print s
            println "*** end command"

        }
    }

    @Test
    @Ignore
    void testCreateProject() {
        println execCommand(["create", "project"])
    }

}

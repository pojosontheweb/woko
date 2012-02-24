package woko.tooling

import junit.framework.Assert
import org.junit.Test
import woko.tooling.utils.Log
import org.junit.Ignore

/**
 * @author Alexis Boissonnat - alexis.boissonnat 'at' gmail.com
 */
class LauncherTest {

    private static Log log = new Log(true)
    private static Launcher launcher = new Launcher(log)

    @Test
    @Ignore
    void withoutArgs() {
        println "Without args :"
        launcher.main(new String[0])
        Assert.assertTrue(log.content.contains('woko: missing argument file'))
        separator()
    }

    @Test
    @Ignore
    void withBadArgs() {
        println "With bad args :"
        String[] args = new String[1]
        args[0] = "badArgsForSure"
        launcher.main(args)
        Assert.assertTrue(log.content.contains('woko: bad arguments'))
        separator()
    }

    @Test
    @Ignore
    void helpWithH() {
        println "With -h :"
        String[] args = new String[1]
        args[0] = "-h"
        launcher.main(args)
        Assert.assertTrue(log.content.contains('Usage :'))
        separator()
    }


    @Test
    @Ignore
    void helpWithHelp() {
        println "With --help :"
        String[] arguments = new String[1]
        arguments[0] = "--help"
        launcher.main(arguments)
        Assert.assertTrue(log.content.contains('Usage :'))
        separator()
    }

    @Test
    @Ignore
    void createWithoutCreationType() {
        println "With create (without creation type) :"
        String[] arguments = new String[1]
        arguments[0] = "create"
        launcher.main(arguments)
        Assert.assertTrue(log.content.contains('woko: you need to specify what you want to create'))
        separator()
    }

    @Test
    @Ignore
    void createWithWrongCreationType() {
        println "With create badCreationType :"
        String[] arguments = new String[2]
        arguments[0] = "create"
        arguments[1] = "badCreationType"
        launcher.main(arguments)
        Assert.assertTrue(log.content.contains('woko: unkown creation type'))
        separator()
    }

    void separator(){
        println '\n*************************************************\n'
    }
}

/*
 * Copyright 2001-2010 Remi Vankeisbelck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package woko.tooling.cli

import org.junit.Test
import org.junit.Before

import static junit.framework.Assert.assertEquals;

import org.junit.rules.TemporaryFolder
import org.junit.Rule

class ScriptTest {

    private String getTestAppDir() {
        String woko2dir = System.getProperty("user.dir")
        if (woko2dir.endsWith("tooling")) {
            woko2dir = woko2dir[0..woko2dir.length()-(File.separator+"tooling").length()]
        }
        println "*** $woko2dir"
        return woko2dir + File.separator+"tooling" +File.separator+ "src" +File.separator+ "main" +File.separator+ "scripts"
    }

    def execCommand(args, workingDir) {
        println "*** $workingDir"
        def process = args.join(" ").execute(null, workingDir)
        return process.text
    }

    void assertCommandResult(args,expectedResult, workingDir) {
        println "Executing $args"
        def actual = execCommand(args, workingDir)
        println actual
        assertEquals("invalid command result for args $args", expectedResult, actual)
    }


    static final String EXPECTED_NO_ARGS =
        "__       __     _  __\n" +
        "\\ \\  _  / /___ | |/ / ___\n" +
        " \\ \\/ \\/ // o \\|   K /   \\\n" +
        "  \\__W__/ \\___/|_|\\_\\\\_o_/  2.0\n" +
        "             POJOs on the Web !\n" +
        "\n" +
        "Usage: woko init\n" +
        "-n project name, serves as the artifact Id (i.e. myapp)\n" +
        "-m the maven group Id (i.e. com.myexample.myapp)\n" +
        "-p the default package name, defaults to maven group Id\n" +
        "-v the version (i.e. 1.0-SNAPSHOT)\n" +
        "-b use Boostrap css & js, defaults to no\n" +
        "-g use Groovy, defaults to no\n" +
        "-h this help\n"

    @Rule public TemporaryFolder folder = new TemporaryFolder()

    String woko
    
    @Before
    void setUp(){
        woko = System.getProperty('os.name').toLowerCase().contains("windows") ? "woko.bat" : "woko"
    }

    @Test
    void testHelp() {
        assertCommandResult([testAppDir+File.separator+woko,"init","-h"], EXPECTED_NO_ARGS, folder.getRoot())
    }

    @Test
    void testInitBootstrapGroovy() {
        assertCommandResult([testAppDir+File.separator+woko, "init","-n","myapp", "-m", "foo.bar.myapp", "-p", "foo.bar", "-v", "1.0-SNAPSHOT"],
                "__       __     _  __\n" +
                "\\ \\  _  / /___ | |/ / ___\n" +
                " \\ \\/ \\/ // o \\|   K /   \\\n" +
                "  \\__W__/ \\___/|_|\\_\\\\_o_/  2.0\n" +
                "             POJOs on the Web !\n" +
                "\n" +
                "Initializing project\n" +
                "| Generating your project, please wait, it can take a while to download everything...\n" +
                "|  - web.xml file created : src" + File.separator + "main" + File.separator + "webapp" + File.separator + "WEB-INF" + File.separator + "web.xml\n" +
                "|  - Layout facet created : foo.bar.facets.MyLayout\n" +
                "|  - resource bundle created : src" + File.separator + "main" + File.separator + "resources" + File.separator + "application.properties\n" +
                "|  \n" +
                "|  Your project has been generated in : myapp \n" +
                "|  Run 'woko start' in order to launch your app in a local Jetty container\n"
                , folder.getRoot())

    }

    @Test
    void testInitNoBootstrapGroovy() {
        assertCommandResult([testAppDir+File.separator+woko, "init","-n","myapp", "-m", "foo.bar.myapp", "-p", "foo.bar", "-v", "1.0-SNAPSHOT", "-b"],
                "__       __     _  __\n" +
                "\\ \\  _  / /___ | |/ / ___\n" +
                " \\ \\/ \\/ // o \\|   K /   \\\n" +
                "  \\__W__/ \\___/|_|\\_\\\\_o_/  2.0\n" +
                "             POJOs on the Web !\n" +
                "\n" +
                "Initializing project\n" +
                "| Generating your project, please wait, it can take a while to download everything...\n" +
                "|  - web.xml file created : src" + File.separator + "main" + File.separator + "webapp" + File.separator + "WEB-INF" + File.separator + "web.xml\n" +
                "|  - Layout facet created : foo.bar.facets.MyLayout\n" +
                "|  - resource bundle created : src" + File.separator + "main" + File.separator + "resources" + File.separator + "application.properties\n" +
                "|  \n" +
                "|  Your project has been generated in : myapp \n" +
                "|  Run 'woko start' in order to launch your app in a local Jetty container\n"
                , folder.getRoot())

    }

    @Test
    void testInitBootstrapNoGroovy() {
        assertCommandResult([testAppDir+File.separator+woko, "init","-n","myapp", "-m", "foo.bar.myapp", "-p", "foo.bar", "-v", "1.0-SNAPSHOT", "-g"],
                "__       __     _  __\n" +
                "\\ \\  _  / /___ | |/ / ___\n" +
                " \\ \\/ \\/ // o \\|   K /   \\\n" +
                "  \\__W__/ \\___/|_|\\_\\\\_o_/  2.0\n" +
                "             POJOs on the Web !\n" +
                "\n" +
                "Initializing project\n" +
                "| Generating your project, please wait, it can take a while to download everything...\n" +
                "|  You will use pure Java\n" +
                "|  - web.xml file created : src" + File.separator + "main" + File.separator + "webapp" + File.separator + "WEB-INF" + File.separator + "web.xml\n" +
                "|  - Layout facet created : foo.bar.facets.MyLayout\n" +
                "|  - resource bundle created : src" + File.separator + "main" + File.separator + "resources" + File.separator + "application.properties\n" +
                "|  \n" +
                "|  Your project has been generated in : myapp \n" +
                "|  Run 'woko start' in order to launch your app in a local Jetty container\n"
                , folder.getRoot())

    }

    @Test
    void testInitNoBootstrapNoGroovy() {
        assertCommandResult([testAppDir+File.separator+woko, "init","-n","myapp", "-m", "foo.bar.myapp", "-p", "foo.bar", "-v", "1.0-SNAPSHOT", "-g", "-b"],
                "__       __     _  __\n" +
                "\\ \\  _  / /___ | |/ / ___\n" +
                " \\ \\/ \\/ // o \\|   K /   \\\n" +
                "  \\__W__/ \\___/|_|\\_\\\\_o_/  2.0\n" +
                "             POJOs on the Web !\n" +
                "\n" +
                "Initializing project\n" +
                "| Generating your project, please wait, it can take a while to download everything...\n" +
                "|  You will use pure Java\n" +
                "|  - web.xml file created : src" + File.separator + "main" + File.separator + "webapp" + File.separator + "WEB-INF" + File.separator + "web.xml\n" +
                "|  - Layout facet created : foo.bar.facets.MyLayout\n" +
                "|  - resource bundle created : src" + File.separator + "main" + File.separator + "resources" + File.separator + "application.properties\n" +
                "|  \n" +
                "|  Your project has been generated in : myapp \n" +
                "|  Run 'woko start' in order to launch your app in a local Jetty container\n"
                , folder.getRoot())

    }


}

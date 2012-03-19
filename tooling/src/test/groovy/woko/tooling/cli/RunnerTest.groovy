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
import org.junit.Ignore
import woko.tooling.utils.Logger
import static junit.framework.Assert.*
import org.junit.rules.TemporaryFolder
import org.junit.Rule

class RunnerTest {

    private String getTestAppDir() {
        String woko2dir = System.getProperty("user.dir")
        if (woko2dir.endsWith("tooling")) {
            woko2dir = woko2dir[0..woko2dir.length()-"/tooling".length()]
        }
        println "*** $woko2dir"
        return woko2dir + "/webtests/webtests-container-auth" // assume we run test from top level woko2 folder
    }

    def execCommand(args, workingDir) {
        Writer sw = new StringWriter()
        new Runner(new Logger(sw), workingDir).run(args)
        sw.flush()
        sw.close()
        sw.toString()
    }

    void assertCommandResult(args,expectedResult) {
        assertCommandResult(args, expectedResult, new File(testAppDir))
    }

    void assertCommandResult(args,expectedResult, workingDir) {
        println "Executing $args"
        def actual = execCommand(args, workingDir)
        println actual
        assertEquals("invalid command result for args $args", expectedResult, actual)
    }


    static final String EXPECTED_NO_ARGS = """Usage : woko <command> arg*

Available commands :

  - list facets|roles		:		list facets or roles
  - create facet|entity		:		create project elements
  - push [resources|quiet]		:		pushes the local facets to a remote application
  - init 		:		Initialize a new Woko project
  - start 		:		run the application in a local jetty container
  - stop 		:		stop the local jetty container (in case started in background process)
  - build 		:		rebuilds the whole application
  - help [command_name]		:		display help about specified command

"""


    @Test
    void testNoArgs() {
        assertCommandResult([], EXPECTED_NO_ARGS)
    }

    @Test
    void testHelp() {
        assertCommandResult(["help"], EXPECTED_NO_ARGS)
    }

    @Test
    void testList() {
        assertCommandResult(["list"], """ERROR : invalid list command
Help for command 'list' : list facets or roles

Usage :

 - woko list facets|roles

Lists components of your application by using runtime information.
The command accepts one argument that can be  :
  * facets   : inits the facets of your app and lists them
  * roles    : lists all the roles defined in your application facets
""")
    }

    @Test
    void testListFacets() {
        assertCommandResult(["list", "facets"], """41 facets found : \n  create, developer, java.lang.Object, woko.facets.builtin.developer.Create
  delete, developer, java.lang.Object, woko.facets.builtin.developer.DeleteImpl
  edit, developer, java.lang.Object, woko.facets.builtin.developer.EditImpl
  find, developer, java.lang.Object, woko.facets.builtin.developer.Find
  groovy, developer, java.lang.Object, woko.facets.builtin.developer.Groovy
  home, all, java.lang.Object, woko.facets.builtin.all.HomeImpl
  home, developer, java.lang.Object, woko.facets.builtin.developer.HomeImpl
  json, developer, java.lang.Object, woko.facets.builtin.developer.JsonImpl
  layout, all, java.lang.Object, woko.facets.builtin.all.LayoutAll
  list, developer, java.lang.Object, woko.facets.builtin.developer.ListImpl
  logout, all, java.lang.Object, woko.facets.builtin.all.LogoutImpl
  navBar, all, java.lang.Object, woko.facets.builtin.all.NavBarAll
  navBar, developer, java.lang.Object, woko.facets.builtin.developer.NavBarDev
  renderLinks, all, java.lang.Object, woko.facets.builtin.all.RenderLinksImpl
  renderLinksEdit, all, java.lang.Object, woko.facets.builtin.all.RenderLinksEditImpl
  renderObject, all, java.lang.Object, woko.facets.builtin.all.RenderObjectImpl
  renderObjectEdit, all, java.lang.Object, woko.facets.builtin.all.RenderObjectEditImpl
  renderObjectJson, all, java.lang.Object, woko.facets.builtin.all.RenderObjectJsonImpl
  renderObjectJson, all, net.sourceforge.stripes.validation.ValidationErrors, woko.facets.builtin.all.RenderValidationErrorsJson
  renderProperties, all, java.lang.Object, woko.facets.builtin.all.RenderPropertiesImpl
  renderPropertiesEdit, all, java.lang.Object, woko.facets.builtin.all.RenderPropertiesEditImpl
  renderPropertyName, all, java.lang.Object, woko.facets.builtin.all.RenderPropertyNameImpl
  renderPropertyValue, all, java.lang.Object, woko.facets.builtin.all.RenderPropertyValueImpl
  renderPropertyValue, all, java.util.Date, woko.facets.builtin.all.RenderPropertyValueDate
  renderPropertyValue, all, java.util.Collection, woko.facets.builtin.all.RenderPropertyValueCollection
  renderPropertyValueEdit, all, java.lang.Number, woko.facets.builtin.all.RenderPropertyValueEditStripesText
  renderPropertyValueEdit, all, java.lang.String, woko.facets.builtin.all.RenderPropertyValueEditStripesText
  renderPropertyValueEdit, all, java.util.Date, woko.facets.builtin.all.RenderPropertyValueEditDate
  renderPropertyValueJson, all, java.lang.Boolean, woko.facets.builtin.all.RenderPropertyValueJsonBasicTypes
  renderPropertyValueJson, all, java.lang.Class, woko.facets.builtin.all.RenderPropertyValueJsonClass
  renderPropertyValueJson, all, java.lang.Number, woko.facets.builtin.all.RenderPropertyValueJsonBasicTypes
  renderPropertyValueJson, all, java.lang.Object, woko.facets.builtin.all.RenderPropertyValueJsonObject
  renderPropertyValueJson, all, java.lang.String, woko.facets.builtin.all.RenderPropertyValueJsonBasicTypes
  renderPropertyValueJson, all, java.util.Date, woko.facets.builtin.all.RenderPropertyValueJsonDate
  renderPropertyValueJson, all, java.util.Collection, woko.facets.builtin.all.RenderPropertyValueJsonCollection
  renderTitle, all, java.lang.Object, woko.facets.builtin.all.RenderTitleImpl
  save, developer, java.lang.Object, woko.facets.builtin.developer.SaveImpl
  search, developer, java.lang.Object, woko.facets.builtin.developer.SearchImpl
  studio, developer, java.lang.Object, woko.facets.builtin.developer.WokoStudio
  toString, developer, java.lang.Object, woko.facets.builtin.developer.ToString
  view, developer, java.lang.Object, woko.facets.builtin.developer.ViewImpl
""")
    }

    @Test
    void testListRoles() {
        assertCommandResult(["list", "roles"], """2 role(s) used in faced keys :
  all
  developer
""")
    }

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    void testInitHelp() {

        println folder.getRoot().absolutePath

        // copy testAppDir/pom.xml into folder
        // straight copy (not optimally efficient)

        folder.newFile('pom.xml').withWriter { file ->
            new File(testAppDir+'/pom.xml').eachLine { line ->
                file.writeLine(line)
            }
        }

       // System.setProperty("user.dir", folder.getRoot().absolutePath);

        assertCommandResult(["init","-help"],
               "usage: woko init\n" +
               " -b,--use-boostrap <yes|no>                      boostrap usage\n" +
               " -g,--use-groovy <yes|no>                        groovy usage\n" +
               " -h,--help                                       Show usage information\n" +
               " -p,--default-package-name <com.example.myapp>   default package name\n"
               ,folder.getRoot())

    }

    @Test
    void testInit() {

        println folder.getRoot().absolutePath

        // copy testAppDir/pom.xml into folder
        // straight copy (not optimally efficient)

        folder.newFile('pom.xml').withWriter { file ->
            new File(testAppDir+'/pom.xml').eachLine { line ->
                file.writeLine(line)
            }
        }

        assertCommandResult(["init","-b","yes", "-g", "no", "-p", "foo.bar"],
                "|  You will use pure Java\n" +
                        "|  - web.xml file created : src/main/webapp/WEB-INF/web.xml\n" +
                        "|  - Layout facet created : foo.bar.facets.MyLayout\n" +
                        "|  - resource bundle created : src/main/resources/application.properties\n" +
                        "|  \n" +
                        "|  Your project has been generated in : $folder.root.name \n" +
                        "|  Run 'woko start' in order to launch your app in a local Jetty container\n"
                , folder.getRoot())

        println 'coucou'
    }



}

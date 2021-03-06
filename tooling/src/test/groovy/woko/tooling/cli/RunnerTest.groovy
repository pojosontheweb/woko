/*
 * Copyright 2001-2012 Remi Vankeisbelck
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
import woko.tooling.utils.Logger
import static junit.framework.Assert.*
import org.junit.rules.TemporaryFolder
import org.junit.Rule
import org.junit.Assume
import org.junit.Ignore
import woko.tooling.cli.commands.MANode
import woko.actions.WokoFacetBindingPolicyManager

class RunnerTest {

    private String getTestAppDir() {
        String woko2dir = System.getProperty("user.dir")
        if (woko2dir.endsWith("tooling")) {
            woko2dir = woko2dir[0..woko2dir.length() - (File.separator + "tooling").length()]
        }
        return woko2dir + File.separator + "webtests" + File.separator + "webtests-container-auth" // assume we run test from top level woko2 folder
    }

    def execCommand(args, workingDir) {
        Writer sw = new StringWriter()
        String[] argsA = new String[args.size()]
        new Runner(new Logger(sw), workingDir).run(args.toArray(argsA))
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

  - list      facets|roles|bindings                         : list various application stuff
  - create    facet|entity                                  : create project elements
  - crud      [<Entity> [<Role> [quiet]                     : Generate the CRUD facets for a given role (view/edit/save/delete)
  - push      [resources|quiet][[[url] username] password]  : pushes the local facets to a remote application
  - generate                                                : Generates a new Woko project
  - start                                                   : run the application in a local jetty container
  - stop                                                    : stop the local jetty container (in case started in background process)
  - build                                                   : rebuilds the whole application
  - env       list|use <env_name>                           : manage the environments
  - groovy    <file> [url]                                  : execute a groovy script on the server
  - help      [command_name]                                : display help about specified command

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
Help for command 'list' : list various application stuff

Usage :

 - woko list facets|roles|bindings

Lists components of your application by using runtime information.
The command accepts one argument that can be  :
  * facets   : inits the facets of your app and lists them
  * roles    : lists all the roles defined in your application facets
  * bindings : lists all possible HTTP request bindings on your facets and action beans
""")
    }

    @Test
    void testListFacets() {
        assertCommandResult(["list", "facets"], """49 facets found : \n  create, developer, java.lang.Object, woko.facets.builtin.developer.Create
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
  renderListItem, all, java.lang.Object, woko.facets.builtin.all.RenderListItemImpl
  renderListTitle, all, java.lang.Object, woko.facets.builtin.all.RenderListTitleImpl
  renderObject, all, java.lang.Object, woko.facets.builtin.all.RenderObjectImpl
  renderObjectEdit, all, java.lang.Object, woko.facets.builtin.all.RenderObjectEditImpl
  renderObjectJson, all, java.lang.Object, woko.facets.builtin.all.RenderObjectJsonImpl
  renderObjectJson, all, net.sourceforge.stripes.validation.ValidationErrors, woko.facets.builtin.all.RenderValidationErrorsJson
  renderProperties, all, java.lang.Object, woko.facets.builtin.all.RenderPropertiesImpl
  renderPropertiesEdit, all, java.lang.Object, woko.facets.builtin.all.RenderPropertiesEditImpl
  renderPropertiesEditButtons, all, java.lang.Object, woko.facets.builtin.all.RenderPropertiesEditButtonsImpl
  renderPropertyName, all, java.lang.Object, woko.facets.builtin.all.RenderPropertyNameImpl
  renderPropertyValue, all, java.lang.Object, woko.facets.builtin.all.RenderPropertyValueImpl
  renderPropertyValue, all, java.util.Date, woko.facets.builtin.all.RenderPropertyValueDate
  renderPropertyValue, all, java.util.Collection, woko.facets.builtin.all.RenderPropertyValueCollection
  renderPropertyValueEdit, all, java.lang.Boolean, woko.facets.builtin.all.RenderPropertyValueEditBoolean
  renderPropertyValueEdit, all, java.lang.Number, woko.facets.builtin.all.RenderPropertyValueEditStripesText
  renderPropertyValueEdit, all, java.lang.Object, woko.facets.builtin.all.RenderPropertyValueEditXToOneRelation
  renderPropertyValueEdit, all, java.lang.String, woko.facets.builtin.all.RenderPropertyValueEditStripesText
  renderPropertyValueEdit, all, java.util.Date, woko.facets.builtin.all.RenderPropertyValueEditDate
  renderPropertyValueEdit, all, java.util.Collection, woko.facets.builtin.all.RenderPropertyValueEditXToManyRelation
  renderPropertyValueJson, all, java.lang.Boolean, woko.facets.builtin.all.RenderPropertyValueJsonBasicTypes
  renderPropertyValueJson, all, java.lang.Class, woko.facets.builtin.all.RenderPropertyValueJsonClass
  renderPropertyValueJson, all, java.lang.Number, woko.facets.builtin.all.RenderPropertyValueJsonBasicTypes
  renderPropertyValueJson, all, java.lang.Object, woko.facets.builtin.all.RenderPropertyValueJsonObject
  renderPropertyValueJson, all, java.lang.String, woko.facets.builtin.all.RenderPropertyValueJsonBasicTypes
  renderPropertyValueJson, all, java.util.Date, woko.facets.builtin.all.RenderPropertyValueJsonDate
  renderPropertyValueJson, all, java.util.Collection, woko.facets.builtin.all.RenderPropertyValueJsonCollection
  renderPropertyValueJson, all, java.util.Map, woko.facets.builtin.all.RenderPropertyValueJsonMap
  renderTitle, all, java.lang.Object, woko.facets.builtin.all.RenderTitleImpl
  renderTitleEdit, all, java.lang.Object, woko.facets.builtin.all.RenderTitleEditImpl
  save, developer, java.lang.Object, woko.facets.builtin.developer.SaveImpl
  search, developer, java.lang.Object, woko.facets.builtin.developer.SearchImpl
  studio, developer, java.lang.Object, woko.facets.builtin.developer.WokoStudio
  toString, developer, java.lang.Object, woko.facets.builtin.developer.ToString
  view, developer, java.lang.Object, woko.facets.builtin.developer.ViewImpl
""")
    }

    @Test
    void testNonExistingCommand() {
        def actual = execCommand(["blah"], new File(testAppDir))
        assertTrue("expected text not found", actual.contains("Command 'blah' not found"))
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
    void testGenerateHelp() {

        boolean isWindows = System.getProperty('os.name').toLowerCase().contains("windows")
        Assume.assumeTrue(!isWindows)

        println folder.getRoot().absolutePath

        // copy testAppDir/pom.xml into folder
        // straight copy (not optimally efficient)

        folder.newFile('pom.xml').withWriter { file ->
            new File(this.class.getResource('/test-pom.xml').toURI()).eachLine { line ->
                file.writeLine(line)
            }
        }

       // System.setProperty("user.dir", folder.getRoot().absolutePath);

        assertCommandResult(["generate","-help"],
               "usage: woko generate\n" +
               " -b,--use-boostrap <yes|no>                      boostrap usage\n" +
               " -g,--use-groovy <yes|no>                        groovy usage\n" +
               " -h,--help                                       Show usage information\n" +
               " -p,--default-package-name <com.example.myapp>   default package name\n" +
               " -r,--registration <yes|no>                      use registration\n"
               ,folder.getRoot())

    }

    @Test
    void testGenerateBootstapNoGroovy() {
        // copy testAppDir/pom.xml into folder
        // straight copy (not optimally efficient)

        folder.newFile('pom.xml').withWriter { file ->
            new File(this.class.getResource('/test-pom.xml').toURI()).eachLine { line ->
                file.writeLine(line)
            }
        }

        assertCommandResult(["generate","-b","yes", "-g", "no", "-p", "foo.bar", "-r", "yes"],
                "|  You will use pure Java\n" +
                "|  - web.xml file created : src" + File.separator + "main"+File.separator+"webapp"+File.separator+"WEB-INF"+File.separator+"web.xml\n" +
                "|  - Layout facet created : foo.bar.facets.MyLayout\n" +
                "|  - resource bundle created : src"+File.separator+"main"+File.separator+"resources"+File.separator+"application.properties\n" +
                "|  \n" +
                "|  Your project has been generated in : $folder.root.name \n" +
                "|  Run 'woko start' in order to launch your app in a local Jetty container\n"
                , folder.getRoot())

    }

    @Test
    void testGenerateNoBootstapGroovy() {
        // copy testAppDir/pom.xml into folder
        // straight copy (not optimally efficient)

        folder.newFile('pom.xml').withWriter { file ->
            new File(this.class.getResource('/test-pom.xml').toURI()).eachLine { line ->
                file.writeLine(line)
            }
        }

        assertCommandResult(["generate","-b","no", "-g", "yes", "-p", "foo.bar", "-r", "yes"],
                        "|  - web.xml file created : src" + File.separator + "main" + File.separator + "webapp" + File.separator + "WEB-INF" + File.separator + "web.xml\n" +
                        "|  - Layout facet created : foo.bar.facets.MyLayout\n" +
                        "|  - resource bundle created : src" + File.separator + "main" + File.separator + "resources" + File.separator + "application.properties\n" +
                        "|  \n" +
                        "|  Your project has been generated in : $folder.root.name \n" +
                        "|  Run 'woko start' in order to launch your app in a local Jetty container\n"
                , folder.getRoot())

    }

    @Test
    void testGenerateNoBootstapNoGroovy() {
        // copy testAppDir/pom.xml into folder
        // straight copy (not optimally efficient)

        folder.newFile('pom.xml').withWriter { file ->
            new File(this.class.getResource('/test-pom.xml').toURI()).eachLine { line ->
                file.writeLine(line)
            }
        }

        assertCommandResult(["generate","-b","no", "-g", "no", "-p", "foo.bar", "-r", "yes"],
                "|  You will use pure Java\n" +
                        "|  - web.xml file created : src" + File.separator + "main" + File.separator + "webapp" + File.separator + "WEB-INF" + File.separator + "web.xml\n" +
                        "|  - Layout facet created : foo.bar.facets.MyLayout\n" +
                        "|  - resource bundle created : src" + File.separator + "main" + File.separator + "resources" + File.separator + "application.properties\n" +
                        "|  \n" +
                        "|  Your project has been generated in : $folder.root.name \n" +
                        "|  Run 'woko start' in order to launch your app in a local Jetty container\n"
                , folder.getRoot())

    }

    @Test
    void testGenerateBootstapGroovy() {
        // copy testAppDir/pom.xml into folder
        // straight copy (not optimally efficient)

        folder.newFile('pom.xml').withWriter { file ->
            new File(this.class.getResource('/test-pom.xml').toURI()).eachLine { line ->
                file.writeLine(line)
            }
        }

        assertCommandResult(["generate","-b","yes", "-g", "yes", "-p", "foo.bar", "-r", "yes"],
                        "|  - web.xml file created : src" + File.separator + "main" + File.separator + "webapp" + File.separator + "WEB-INF" + File.separator + "web.xml\n" +
                        "|  - Layout facet created : foo.bar.facets.MyLayout\n" +
                        "|  - resource bundle created : src" + File.separator + "main" + File.separator + "resources" + File.separator + "application.properties\n" +
                        "|  \n" +
                        "|  Your project has been generated in : $folder.root.name \n" +
                        "|  Run 'woko start' in order to launch your app in a local Jetty container\n"
                , folder.getRoot())

    }

    @Test
    void testListBindings() {
        assertCommandResult(["list", "bindings"], """Listing bindings, facet packages :
  - test.facet.pkg
  - facets
  - woko.facets.builtin

(create,developer,java.lang.Object) [woko.facets.builtin.developer.Create] object.*
=> Found 1 accessible binding(s) in facet (create,developer,java.lang.Object) [woko.facets.builtin.developer.Create]

(delete,developer,java.lang.Object) [woko.facets.builtin.developer.DeleteImpl] facet.cancel
(delete,developer,java.lang.Object) [woko.facets.builtin.developer.DeleteImpl] facet.confirm
=> Found 2 accessible binding(s) in facet (delete,developer,java.lang.Object) [woko.facets.builtin.developer.DeleteImpl]

(edit,developer,java.lang.Object) [woko.facets.builtin.developer.EditImpl] object.*
=> Found 1 accessible binding(s) in facet (edit,developer,java.lang.Object) [woko.facets.builtin.developer.EditImpl]

(find,developer,java.lang.Object) [woko.facets.builtin.developer.Find] object.*
=> Found 1 accessible binding(s) in facet (find,developer,java.lang.Object) [woko.facets.builtin.developer.Find]

(groovy,developer,java.lang.Object) [woko.facets.builtin.developer.Groovy] object.*
(groovy,developer,java.lang.Object) [woko.facets.builtin.developer.Groovy] facet.code
=> Found 2 accessible binding(s) in facet (groovy,developer,java.lang.Object) [woko.facets.builtin.developer.Groovy]

(home,developer,java.lang.Object) [woko.facets.builtin.developer.HomeImpl] object.*
=> Found 1 accessible binding(s) in facet (home,developer,java.lang.Object) [woko.facets.builtin.developer.HomeImpl]

(json,developer,java.lang.Object) [woko.facets.builtin.developer.JsonImpl] object.*
=> Found 1 accessible binding(s) in facet (json,developer,java.lang.Object) [woko.facets.builtin.developer.JsonImpl]

(list,developer,java.lang.Object) [woko.facets.builtin.developer.ListImpl] object.*
(list,developer,java.lang.Object) [woko.facets.builtin.developer.ListImpl] facet.page
(list,developer,java.lang.Object) [woko.facets.builtin.developer.ListImpl] facet.resultsPerPage
=> Found 3 accessible binding(s) in facet (list,developer,java.lang.Object) [woko.facets.builtin.developer.ListImpl]

(save,developer,java.lang.Object) [woko.facets.builtin.developer.SaveImpl] object.*
=> Found 1 accessible binding(s) in facet (save,developer,java.lang.Object) [woko.facets.builtin.developer.SaveImpl]

(search,developer,java.lang.Object) [woko.facets.builtin.developer.SearchImpl] object.*
(search,developer,java.lang.Object) [woko.facets.builtin.developer.SearchImpl] facet.page
(search,developer,java.lang.Object) [woko.facets.builtin.developer.SearchImpl] facet.query
(search,developer,java.lang.Object) [woko.facets.builtin.developer.SearchImpl] facet.resultsPerPage
=> Found 4 accessible binding(s) in facet (search,developer,java.lang.Object) [woko.facets.builtin.developer.SearchImpl]

(studio,developer,java.lang.Object) [woko.facets.builtin.developer.WokoStudio] object.*
(studio,developer,java.lang.Object) [woko.facets.builtin.developer.WokoStudio] facet.facetDescriptors[]
(studio,developer,java.lang.Object) [woko.facets.builtin.developer.WokoStudio] facet.facetDescriptors[].name
(studio,developer,java.lang.Object) [woko.facets.builtin.developer.WokoStudio] facet.facetDescriptors[].profileId
=> Found 4 accessible binding(s) in facet (studio,developer,java.lang.Object) [woko.facets.builtin.developer.WokoStudio]

(toString,developer,java.lang.Object) [woko.facets.builtin.developer.ToString] object.*
=> Found 1 accessible binding(s) in facet (toString,developer,java.lang.Object) [woko.facets.builtin.developer.ToString]

(view,developer,java.lang.Object) [woko.facets.builtin.developer.ViewImpl] object.*
=> Found 1 accessible binding(s) in facet (view,developer,java.lang.Object) [woko.facets.builtin.developer.ViewImpl]

Scanning for Action Beans in packages :
  - some.test.pkg
  - woko.actions

some.test.pkg.MyAction foo
some.test.pkg.MyAction nested
some.test.pkg.MyAction nested.baz
some.test.pkg.MyAction nestedObjects[]
some.test.pkg.MyAction nestedObjects[].bar
=> Found 5 accessible binding(s) in action bean some.test.pkg.MyAction

Found 28 accessible bindings in the app.
""")
    }

}


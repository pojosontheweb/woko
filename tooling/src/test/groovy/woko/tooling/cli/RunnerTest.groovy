package woko.tooling.cli

import org.junit.Test
import org.junit.Ignore
import woko.tooling.utils.Logger
import static junit.framework.Assert.*

class RunnerTest {

    private String getTestAppDir() {
        System.getProperty("user.dir") + "/webtests/webtests-container-auth" // assume we run test from top level woko2 folder
    }

    def execCommand(args, workingDir) {
        Writer sw = new StringWriter()
        new Runner(new Logger(sw), workingDir).run(args)
        sw.flush()
        sw.close()
        sw.toString()
    }

    void assertCommandResult(args,expectedResult) {
        println "Executing $args"
        def actual = execCommand(args, new File(testAppDir))
        println actual
        assertEquals("invalid command result for args $args", expectedResult, actual)
    }

    static final String EXPECTED_NO_ARGS = """__       __     _  __
\\ \\  _  / /___ | |/ / ___
 \\ \\/ \\/ // o \\|   K /   \\
  \\__W__/ \\___/|_|\\_\\\\_o_/  2.0
             POJOs on the Web !

Usage : woko <command> arg*

Available commands :

  - list facets|roles		:		list facets or roles
  - create facet|entity		:		create project elements
  - start 		:		run the application in a local tomcat container
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
        assertCommandResult(["list"], """__       __     _  __
\\ \\  _  / /___ | |/ / ___
 \\ \\/ \\/ // o \\|   K /   \\
  \\__W__/ \\___/|_|\\_\\\\_o_/  2.0
             POJOs on the Web !

ERROR : invalid list command
Help for command 'list' : list facets or roles

Usage :

 - woko list facets|roles

Lists components of your application by using runtime information.
The command accepts one argument that can be  :
  * facets   : inits the facets of your app and lists them
  * roles    : lists all the roles defined in your application facets
""")
    }

    @Ignore
    @Test
    void testListFacets() {
        assertCommandResult(["list", "facets"], """__       __     _  __
\\ \\  _  / /___ | |/ / ___
 \\ \\/ \\/ // o \\|   K /   \\
  \\__W__/ \\___/|_|\\_\\\\_o_/  2.0
             POJOs on the Web !

40 facets found : \n  create, developer, java.lang.Object, woko.facets.builtin.developer.Create
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

    @Ignore
    @Test
    void testListRoles() {
        assertCommandResult(["list", "roles"], """__       __     _  __
\\ \\  _  / /___ | |/ / ___
 \\ \\/ \\/ // o \\|   K /   \\
  \\__W__/ \\___/|_|\\_\\\\_o_/  2.0
             POJOs on the Web !

2 role(s) used in faced keys :
  all
  developer
""")
    }


}

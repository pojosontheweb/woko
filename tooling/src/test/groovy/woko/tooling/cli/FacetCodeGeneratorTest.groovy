package woko.tooling.cli

import woko.tooling.utils.Logger
import org.junit.Test

class FacetCodeGeneratorTest {

    @Test
    void testGroovyFacetNoSupertypesNoFragment() {
        def out = new ByteArrayOutputStream()
        out.withWriter { sysout ->
            Logger logger = new Logger(sysout)
            new FacetCodeGenerator(logger, new File(""), "myfacet", "myrole", "com.xyz.MyFacetClass").
              setUseGroovy(true).
              generate(sysout)
        }
        println out.toString()
    }


}

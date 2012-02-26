package woko.tooling.utils

import org.junit.Test
import org.apache.maven.model.Model

class TestPomHelper {

    @Test
    void testGetModel(){
        Writer sw = new StringWriter()
        Logger logger = new Logger(sw)
        try{
            PomHelper pm = new PomHelper()
            Model model = pm.getModel()
            assert model.groupId.equals("com.rvkb")
        }catch (RuntimeException e){
            logger.error(e.getMessage())
            print(sw)
        }


    }
}

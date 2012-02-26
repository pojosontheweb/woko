package woko.tooling.utils

import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import org.apache.maven.model.Model
import org.codehaus.plexus.util.xml.pull.XmlPullParserException

class PomHelper {

    Model model

    PomHelper(File pomFile){
        // No need to check if pom exists or not : It's a prerequisite of the command processor itself
        Reader pomReader = new FileReader(pomFile)
        try {
            MavenXpp3Reader modelReader = new MavenXpp3Reader()
            model = modelReader.read(pomReader)
        }catch (IOException e){
            throw new RuntimeException("IOException : Error reading the pom.xml file.")
        }catch ( XmlPullParserException e ){
            throw new RuntimeException( "Your pom.wml seems to be corrupt... Try to fix it before launching woko commands.");
        }finally{
            pomReader.close()
        }
    }
}
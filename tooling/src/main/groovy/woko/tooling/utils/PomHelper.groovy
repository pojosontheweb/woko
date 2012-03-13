package woko.tooling.utils

import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import org.apache.maven.model.Model
import org.codehaus.plexus.util.xml.pull.XmlPullParserException
import org.apache.maven.model.io.xpp3.MavenXpp3Writer
import org.apache.maven.model.Dependency

class PomHelper {

    Model model
    File pomFile

    PomHelper(File pomFile){
        this.pomFile = pomFile
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

    void addDependency(Dependency dependency){

        Writer pomWriter = new FileWriter(pomFile)
        model.addDependency(dependency)
        try{
            MavenXpp3Writer modelWriter = new MavenXpp3Writer()
            modelWriter.write(pomWriter, model)
            pomWriter.flush()
        }catch (IOException e){
            throw new RuntimeException("IOException : Error writing the pom.xml file.")
        }finally{
            pomWriter.close()
        }
    }
}
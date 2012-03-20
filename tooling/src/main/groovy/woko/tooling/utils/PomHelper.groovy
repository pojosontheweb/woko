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

package woko.tooling.utils

import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import org.apache.maven.model.Model
import org.codehaus.plexus.util.xml.pull.XmlPullParserException
import org.apache.maven.model.io.xpp3.MavenXpp3Writer
import org.apache.maven.model.Dependency
import org.apache.maven.model.Plugin

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
        addDependency(dependency, true)
    }

    void addDependency(Dependency dependency, boolean atEnd){
        Writer pomWriter = new FileWriter(pomFile)
        if (atEnd) {
            model.addDependency(dependency)
        } else {
            List<Dependency> deps = model.getDependencies();
            deps.add(0, dependency);
        }
        try{
            MavenXpp3Writer modelWriter = new MavenXpp3Writer()
            modelWriter.write(pomWriter, model)
            pomWriter.flush()
        }catch (IOException e){
            throw new RuntimeException("IOException : Error writing the pom.xml file, , during the dependencies addition.")
        }finally{
            pomWriter.close()
        }
    }

    void addPlugin(Plugin plugin){
        Writer pomWriter = new FileWriter(pomFile)
        model.getBuild().addPlugin(plugin)
        try{
            MavenXpp3Writer modelWriter = new MavenXpp3Writer()
            modelWriter.write(pomWriter, model)
            pomWriter.flush()
        }catch (IOException e){
            throw new RuntimeException("IOException : Error writing the pom.xml file, during the plugin addition")
        }finally{
            pomWriter.close()
        }
    }
}
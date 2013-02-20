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

package com.canoo.webtest

import org.apache.tools.ant.Project
import com.canoo.webtest.util.WebtestEmbeddingUtil
import com.canoo.webtest.groovy.WebTestBuilder
/**
 * Base class providing facility to express tests as Groovy code.
 * @author Marc Guillemot
 */
abstract class WebtestCaseFixed extends GroovyTestCase
{

	static ownerProject
    def ant

    Object getAnt()
	{
		if (!ant)
			createAntBuilder()
		return ant
	}

	void createAntBuilder()
	{
		ant = new WebTestBuilder(getOwnerProject())
	}

	/**
	 * Gets the owner project where the WebTest tasks are defined. If this project hasn't been set previously from
	 * outside, then the test case is probably run from within the IDE and not using webtest.xml or the maven plugin (coming).
	 * In this case we create a new project an initialize it here and we register a shutdownHook for the JVM where the
	 * test results are formatted.
	 */
	Project getOwnerProject() {
		if (!this.ownerProject)
		{
			def antBuilder = new AntBuilder()
			
			def temporaryWebTestResourcesFolder = getTemporaryWebtestResourcesFolder()

			WebtestEmbeddingUtil.copyWebTestResources(temporaryWebTestResourcesFolder)

			antBuilder.path(id: "wt.defineTasks.classpath.id")
			{
				pathelement(path: '${java.class.path}')
			}
			antBuilder.property(name: "~wt.defineTasks.defineClasspath.skip", value: "true")
			antBuilder.property(name: "wt.generateDtd.skip", value: "true")
			antBuilder.property(name: "wt.generateDefinitions.skip", value: "true")
			antBuilder.property(name: "wt.defineMacros.skip", value: "true")
			antBuilder.property(name: "wt.countWebtestResults.skip", value: "true")
			antBuilder.property(name: "wt.junitLikeReports.skip", value: "true")
			antBuilder.property(name: "wt.config.haltonerror", value: "true")
			antBuilder.property(name: "wt.config.haltonfailure", value: "true")
            antBuilder.property(name: "wt.headless", value: System.getProperty("wt.headless", "true"))

            def skipReports = System.getProperty("wt.htmlReports.skip", "true")
            println " *** SKIP REPORTS : $skipReports"

            antBuilder.property(name: "wt.htmlReports.skip", value: skipReports)
            antBuilder.property(name: "wt.config.resultpath", value: new File(temporaryWebTestResourcesFolder.parentFile, "webtest-results"))

			def tmpWebtestXml = new File(temporaryWebTestResourcesFolder, "webtest.xml")

	        antBuilder.'import' (file: tmpWebtestXml)   // sets properties into current ant project
	        antBuilder.project.executeTarget 'wt.before.testInWork'
	        
	        // registration of HTML reports should be done at the end
	        addShutdownHook {
				doAfterTestsWork()
			}
			this.ownerProject = antBuilder.project
		}
		
		this.ownerProject
	}
	
	/**
	 * Called by a shutdown hook to perform actions that have to be performed once all
	 * tests have been executed (ie generation of HTML reports)
	 */
	protected doAfterTestsWork()
	{
		ant.project.executeTarget 'wt.after.testInWork'
	}
	
	/**
	 * Gets the folder where WebTest resources should be copied.
	 * This is used only when a WebTestCase is executed directly (ie not from webtest.xml or from maven) and
	 * has to setup WebTest tasks and Co by itself
	 * @return the webtest-resources subfolder of the (class) root folder where the .class file
	 * of the class extending WebTestCase is located
	 */
	protected File getTemporaryWebtestResourcesFolder() {
		def thisClassFileName = this.class.name.replace('.', File.separator) + ".class"
		println "thisClassFileName: $thisClassFileName"
		def thisClassFileUrl = this.class.classLoader.findResource(thisClassFileName)
		println "thisClassFileUrl: $thisClassFileUrl"
		def thisClassFileFile = new File(thisClassFileUrl.toURI())
		def classFilesDir = new File(thisClassFileFile.absolutePath - thisClassFileName)
		return new File(classFilesDir, "webtest-resources")
	}
	
    void webtest(String name, Closure yield)
	{
		createAntBuilder()
		
        ant.webtest(name: name)
		{
			config()
            yield.delegate = ant
            yield()
        }
    }
	
	void config()
	{
		// default implementation does nothing
	}
}

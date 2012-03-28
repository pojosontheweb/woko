package woko.mojos;

/*
 * Copyright 2001-2012 Remi Vankeisbelck.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @goal envi
 * @phase process-resources
 */
public class EnvironmentMojo extends AbstractMojo {

    public static final String ENVIRONMENTS_FOLDER = "environments";

    /**
     * Location of the build output.
     *
     * @parameter expression="${project.build.directory}"
     * @required
     */
    private File outputDirectory;

    /**
     * Environment to be used
     * @parameter expression="${woko.env}"
     * @required
     */
    private String env;

    /**
     * Project base folder
     * @parameter expression="${project.build.sourceDirectory}"
     */
    private File projectDirectory;

    public void execute()
            throws MojoExecutionException {
        File f = outputDirectory;
        String envPath = projectDirectory.getAbsolutePath() + File.separator +
                ENVIRONMENTS_FOLDER + File.separator + env;
        File envRoot = new File(envPath);
        if (!envRoot.exists()) {
            throw new MojoExecutionException("Environment folder not found : '" + envPath + "'.");
        }
        if (!envRoot.isDirectory()) {
            throw new MojoExecutionException("Environment ain't a valid folder : '" + envPath + "'.");
        }
        // cp -r everything in environments/myenv to target/classes
        getLog().info("Copying resources from environment '" + env + "' : ");
        handleFolder()
        

    }
}

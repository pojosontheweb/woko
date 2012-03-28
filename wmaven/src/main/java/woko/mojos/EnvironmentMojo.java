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
import org.apache.maven.plugin.logging.Log;
import woko.Woko;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @goal env
 * @phase process-resources
 */
public class EnvironmentMojo extends AbstractMojo {

    public static final String ENVIRONMENTS_FOLDER = "environments";

    /**
     * Location of the build output.
     *
     * @parameter expression="${project.build.outputDirectory}"
     * @required
     */
    private File outputDirectory;

    /**
     * Environment to be used
     * @parameter expression="${woko.env}"
     */
    private String env;

    /**
     * Project base folder
     * @parameter expression="${basedir}"
     */
    private File projectDirectory;

    public void execute()
            throws MojoExecutionException {
        File enviFile = new File(outputDirectory.getAbsolutePath() + File.separator + Woko.ENVI_FILE);
        if (enviFile.exists()) {
            enviFile.delete();
        }
        if (env!=null) {
            FileWriter out = null;
            try {
                try {
                    out = new FileWriter(enviFile);
                    out.write(env);
                } finally {
                    if (out!=null) {
                        out.flush();
                        out.close();
                    }
                }
                final Log log = getLog();
                String envPath = projectDirectory.getAbsolutePath() + File.separator +
                        ENVIRONMENTS_FOLDER + File.separator + env;
                File envRoot = new File(envPath);
                if (!envRoot.exists()) {
                    throw new MojoExecutionException("Environment folder not found : '" + envPath + "'. Make sure you have " +
                        "an 'environments/" + env + "' folder in your project root.");
                } else {
                    if (!envRoot.isDirectory()) {
                        throw new MojoExecutionException("Environment ain't a folder : '" + envPath + "'.");
                    }
                    // cp -r everything in environments/myenv to target/classes
                    log.info("Copying resources from environment '" + env + "' to '" + outputDirectory.getAbsolutePath() + "' :");
                    FolderCopy.copy(envRoot, outputDirectory, new FolderCopy.CopyCallback() {
                        @Override
                        public void onCopy(File fromDir, File toDir, File f) {
                            log.info("  " + f.getPath());
                        }
                    });
                }
            } catch (IOException e) {
                throw new MojoExecutionException("unable to copy environment file(s) for environment '" + env + "'", e);
            }
        }
    }

}

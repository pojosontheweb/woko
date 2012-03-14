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

package com.canoo.webtest.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


/**
 * This class provides utilities to work with WebTest without having the "normal" WebTest distribution but only the jar file(s).
 * @author Marc Guillemot
 */
public class WebtestEmbeddingUtil 
{
	/**
	 * Copies WebTest resources (ie webtest.xml, tools/*, the XSLs, CSSs and pictures, ...) package in WebTest jar file
	 * into the provide folder. Indeed different Ant tasks can't work with resources in a jar file.
	 * @param targetFolder the folder in which resources should be copied to
	 * @throws java.io.IOException if the resources can be accessed or copied
	 */
	static void copyWebTestResources(final File targetFolder) throws IOException
	{
		final String resourcesPrefix = "com/canoo/webtest/resources/";
		final URL webtestXmlUrl = WebtestEmbeddingUtil.class.getClassLoader().getResource(resourcesPrefix + "webtest.xml");

		if (webtestXmlUrl == null)
		{
			throw new IllegalStateException("Can't find resource " + resourcesPrefix + "webtest.xml");
		}
		else if (webtestXmlUrl.toString().startsWith("jar:file"))
		{
		    final String urlJarFileName = webtestXmlUrl.toString().replaceFirst("^jar:file:([^\\!]*).*$", "$1");
            final String jarFileName = URLDecoder.decode(urlJarFileName, Charset.defaultCharset().name());
			final JarFile jarFile = new JarFile(jarFileName);
			final String urlPrefix = StringUtils.removeEnd(webtestXmlUrl.toString(), "webtest.xml");
			
			for (final Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();)
			{
				final JarEntry jarEntry = entries.nextElement();
				if (jarEntry.getName().startsWith(resourcesPrefix))
				{
					final String relativeName = StringUtils.removeStart(jarEntry.getName(), resourcesPrefix);
					final URL url = new URL(urlPrefix + relativeName);
					final File targetFile = new File(targetFolder, relativeName);
					FileUtils.forceMkdir(targetFile.getParentFile());
					FileUtils.copyURLToFile(url, targetFile);
				}
			}
		}
		else if (webtestXmlUrl.toString().startsWith("file:"))
		{
			// we're probably developing and/or have a custom version of the resources in classpath
			final File webtestXmlFile = FileUtils.toFile(webtestXmlUrl);
			final File resourceFolder = webtestXmlFile.getParentFile();
			
			FileUtils.copyDirectory(resourceFolder, targetFolder);
		} else {
            throw new IllegalStateException("Resource " + resourcesPrefix + "webtest.xml is not in a jar file: " + webtestXmlUrl);
        }
	}
}

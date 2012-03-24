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

package woko.tooling.cli

import woko.tooling.utils.Logger
import groovy.text.GStringTemplateEngine
import woko.tooling.utils.AppUtils

class FacetCodeGenerator {

    private final Logger logger
    private final File baseDir
    private final String facetClassName

    private final def binding = [:]
    private boolean useGroovy = false
    private List<String> imports = []
    private String fragmentPath = null
    private boolean dontGenerate = false

    FacetCodeGenerator(Logger logger, File baseDir, String name, String role, String facetClassName) {
        this.logger = logger
        this.baseDir = baseDir
        this.facetClassName = facetClassName
        binding["name"] = name
        binding["role"] = role
        def pc = AppUtils.extractPkgAndClazz(facetClassName)
        binding["facetPackage"] = pc.pkg
        binding["facetClassName"] = pc.clazz
        binding["targetTypeStr"] = ""
        binding["baseClassStr"] = ""
        binding["interfaceStr"] = ""
        binding["classBody"] = ""
    }

    FacetCodeGenerator setTargetObjectType(String targetType) {
        if (targetType && targetType!="Object" && targetType!="java.lang.Object") {
            def pc = AppUtils.extractPkgAndClazz(targetType)
            binding["targetTypeStr"] = ", targetObjectType=${pc.clazz}.class"
            imports << targetType
        }
        this
    }

    FacetCodeGenerator setBaseClass(Class<?> baseClass) {
        if (baseClass) {
            def pc = AppUtils.extractPkgAndClazz(baseClass.name)
            binding["baseClassStr"] = " extends $pc.clazz"
            imports << baseClass.name
        }
        this
    }

    FacetCodeGenerator setInterface(Class<?> intf) {
        if (intf) {
            def pc = AppUtils.extractPkgAndClazz(intf.name)
            binding["interfaceStr"] = " implements $pc.clazz"
            imports << intf.name
        }
        this
    }

    FacetCodeGenerator setFragmentPath(String path) {
        this.fragmentPath = path
        this
    }

    FacetCodeGenerator setUseGroovy(boolean useGroovy) {
        this.useGroovy = useGroovy
        this
    }

    void generate(Writer out) {
        // compute imports
        StringBuilder importsStr = new StringBuilder()
        imports.each {
            importsStr << "\nimport $it"
            if (!useGroovy) {
                importsStr << ";"
            }
        }
        binding["imports"] = "$importsStr"

        // compute class body
        if (fragmentPath) {
            binding["classBody"] = useGroovy ?
                """
    @Override
    String getPath() {
        "$fragmentPath"
    }
""" :
                """
    @Override
    public String getPath() {
        return "$fragmentPath";
    }
"""
        }

        def engine = new GStringTemplateEngine()
        def tpl = useGroovy ? "facet-groovy.template" : "facet-java.template"
        engine.
          createTemplate(getClass().getResource(tpl)).
          make(binding).
          writeTo(out)

    }

    def generate() {
        if (dontGenerate) {
            logger.indentedLog("Dry run, no files will be written. The source is simply printed here :")
            generate(logger.writer)
            logger.indentedLog(" ")
            return null
        } else {
            def pc = AppUtils.extractPkgAndClazz(facetClassName)
            def srcFolderName = useGroovy ? "groovy" : "java"
            String pkgPath = pc["pkg"].replaceAll("\\.", "/")
            String fullPkgPath = "$baseDir.absolutePath/src/main/$srcFolderName/$pkgPath"
            new File(fullPkgPath).mkdirs()
            String pathToFile = "$fullPkgPath/${pc["clazz"]}.$srcFolderName"
            logger.indentedLog("Generating file $pathToFile")
            new FileOutputStream(pathToFile).withWriter { w ->
                generate(w)
            }
            return pathToFile
        }
    }

    FacetCodeGenerator setDontGenerate(boolean dontGen) {
        this.dontGenerate = dontGen
        this
    }
}

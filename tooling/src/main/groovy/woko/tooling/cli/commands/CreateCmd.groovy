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

package woko.tooling.cli.commands

import woko.tooling.cli.Command
import woko.tooling.utils.Logger
import static woko.tooling.utils.AppUtils.*
import woko.facets.builtin.WokoFacets
import woko.facets.FragmentFacet
import woko.tooling.cli.FacetCodeGenerator
import woko.tooling.cli.Runner

class CreateCmd extends Command {

    CreateCmd(Runner runner, File projectDir, Logger logger) {
        super(
                runner,
                projectDir,
                logger,
                "create",
                "create project elements",
                "facet|entity",
                """Create new elements (facets, entities, etc.) in your Woko project.
Usage involves one argument, depending on the type of element
you want to create :
  * facet    : allows for easy creation of facets and associated views if any.
  * entity   : create persistent POJOs and associated default facets.""")
    }

    @Override
    void execute(List<String> args) {
        String arg0 = getArgAt(args, 0)
        switch (arg0) {
            case "facet" :
                def fdm = getFdm()

                def genFiles = []

                // ask for required params and assist the best we can !

                def name = requiredAsk("Facet name")
                // check if there are facets with this name already
                def facetsWithSameName = fdm.descriptors.findAll { fd -> fd.name == name }
                if (facetsWithSameName) {
                    // facet override !
                    iLog("Found ${facetsWithSameName.size()} facet(s) with the same name :")
                    facetsWithSameName.each { fd ->
                        iLog("  - $fd.name, $fd.profileId, $fd.targetObjectType.name, $fd.facetClass")
                    }
                    iLog("You might be overriding a facet...")
                } else {
                    iLog("No facet(s) with that name already, you are not overriding anything...")
                }
                iLog(" ") // line sep

                def role = askWithDefault("Role", "all")
                // check if the facet already exists for that name and role
                def facetsWithSameNameAndSameRole = facetsWithSameName.findAll { fd -> fd.profileId == role}
                if (facetsWithSameNameAndSameRole) {
                    // facet overide !
                    iLog("Found ${facetsWithSameNameAndSameRole.size()} facet(s) with the same name and role :")
                    facetsWithSameNameAndSameRole.each { fd ->
                        iLog("  - $fd.name, $fd.profileId, $fd.targetObjectType.name, $fd.facetClass")
                    }
                    iLog("You are overriding an existing facet by type...")
                } else {
                    if (role=="all") {
                        iLog("You are assigning the facet to all users of the application...")
                    }
                }
                iLog(" ") // line sep

                def targetType = modelFqcnFromSimpleName(
                        askWithDefault("Target type", "java.lang.Object")
                );

                // check if a facet exists for the same type
                def identicalFacets = facetsWithSameNameAndSameRole.findAll { fd -> fd.targetObjectType.name == targetType }
                if (identicalFacets) {
                    iLog("Found ${identicalFacets.size()} facet(s) with the exact same key :")
                    identicalFacets.each { fd ->
                        iLog("  - $fd.facetClass")
                    }
                    iLog("You are REPLACING a facet. Make sure the ordering of the facet packages is ok : ")
                    computeFacetPackages().each {
                        iLog("  - $it")
                    }
                    iLog(" ") // line sep
                } else {
                    if (facetsWithSameName) {
                        iLog("You are overriding facet(s) by type...")
                        iLog(" ") // line sep
                    }
                }

                // now that we have the params, propose an appropriate base class if any
                def baseClass = WokoFacets.getDefaultImpl(name)
                def baseIntf = WokoFacets.getInterface(name)
                if (baseClass && baseIntf) {
                    iLog("Found interface and possible base class for your facet :")
                    iLog("  - interface  : $baseIntf.name")
                    iLog("  - base class : $baseClass.name")
                } else if (baseClass) {
                    iLog("Found possible base class your facet : $baseClass.name")
                } else if (baseIntf) {
                    iLog("Found interface for your facet : $baseIntf.name")
                }
                if (baseClass) {
                    iLog(" ") // line sep
                    if (yesNoAsk("Do you want to use the base class")) {
                        iLog("Your facet will extend $baseClass.name")
                    }
                } else if (baseIntf) {
                    iLog("Your facet will implement $baseIntf.name")
                }
                iLog(" ") // line sep

                // check if the facet is a fragment facet and propose JSP fragment if any
                def targetObjectSimpleType = extractPkgAndClazz(targetType).clazz
                def fragmentPath = null
                if (baseIntf && FragmentFacet.isAssignableFrom(baseIntf)) {
                    iLog("The facet is a Fragment Facet...")
                    iLog(" ") // line sep
                    if (yesNoAsk("Do you want to generate JSP a fragment")) {
                        String proposedFragmentPath = "/WEB-INF/jsp/$role/${name}" // ${targetObjectSimpleType}.jsp"
                        if (targetObjectSimpleType != "Object") {
                            proposedFragmentPath += targetObjectSimpleType
                        }
                        String capRole = capitalize(role)
                        proposedFragmentPath += capRole + ".jsp"
                        fragmentPath = askWithDefault("Enter the JSP fragment path", proposedFragmentPath)
                    } else {
                        fragmentPath = null
                    }
                }
                // check if there's a base class, and an associated JSP fragment
                // by convention JSP fragments are stored as statics of the facet class
                def baseClassFragmentPath = null
                if (baseClass!=null) {
                    try {
                        baseClassFragmentPath = baseClass.FRAGMENT_PATH
                    } catch(Exception) {
                        iLog("No JSP fragment available from the base class, an empty JSP will be generated.")
                        iLog(" ") // line sep
                    }
                }

                // ask for facet class name with default computed name
                def basePackage = getBaseFacetPackage()
                if (!basePackage){
                    logger.error("unable to find your facet package definition. Please check your web.xml file.")
                    basePackage = simpleAsk("facets pakage ?")
                }

                def capName = capitalize(name)
                String proposedFacetClassName = "${basePackage}.${role}.${capName}"
                if (targetObjectSimpleType != "Object") {
                    proposedFacetClassName += targetObjectSimpleType
                }
                def capRole = capitalize(role)
                proposedFacetClassName += capRole
                def facetClassName = askWithDefault("Specify the facet class name", proposedFacetClassName)

                // do we generate a Groovy or a Java class ?
                // check if we have Groovy available in the project
                def useGroovy = askIfUseGroovy(false)

                // show summary of all infos
                iLog(" ") // line sep
                iLog(" --- Summary ---")
                iLog(" ") // line sep
                iLog(" Facet key         : $name, $role, $targetType")
                iLog(" Facet class       : $facetClassName")
                if (baseIntf) {
                    iLog(" Implements        : ${baseIntf.name}")
                }
                if (baseClass) {
                    iLog(" Extends           : ${baseClass.name}")
                }
                if (fragmentPath) {
                    iLog(" JSP fragment path : $fragmentPath")
                }
                def lang = useGroovy ? "Groovy" : "pure Java"
                iLog(" Facet written in  : $lang")
                iLog(" Facet source dir  : $projectDir.absolutePath/src/main/${useGroovy ? "groovy" : "java"}")
                iLog(" ") // line sep

                boolean doIt = yesNoAsk("Is this OK ? Shall we generate all this (n to view gen sources only)")

                // file generation
                def facetFilePath = new FacetCodeGenerator(logger,projectDir,name,role,facetClassName).
                        setTargetObjectType(targetType).
                        setBaseClass(baseClass).
                        setInterface(baseIntf).
                        setFragmentPath(fragmentPath).
                        setUseGroovy(useGroovy).
                        setDontGenerate(
                                !doIt
                        ).
                        generate()
                if (facetFilePath) {
                    genFiles << facetFilePath
                }

                // create the JSP fragment file if requested
                if (fragmentPath) {

                    def genDefaultFragment = { w ->
                        w << "Default generated JSP fragment for facet \${$name}..."
                        iLog("Default JSP fragment generated.")
                    }

                    def output = logger.writer
                    if (doIt) {
                        int i = fragmentPath.lastIndexOf("/")
                        def pathToJsp = fragmentPath
                        if (i!=-1) {
                            pathToJsp = fragmentPath[0..i-1]
                        }
                        pathToJsp = "$projectDir/src/main/webapp/$pathToJsp"
                        new File(pathToJsp).mkdirs()

                        def genFragmentFullPath = "$projectDir/src/main/webapp/$fragmentPath"
                        output = new FileOutputStream(genFragmentFullPath)
                        genFiles << genFragmentFullPath
                    }

                    if (baseClassFragmentPath) {
                        iLog("Using JSP fragment from base class : ")
                        iLog("  - $baseClassFragmentPath")
                        // look in target folder
                        def finalName = pomHelper.model.build.finalName
                        String path = "$projectDir.absolutePath/target/$finalName$baseClassFragmentPath"
                        def f = new File(path)
                        if (!f.exists()) {
                            iLog("File $path not found : build the project first ?" ) // TODO better error handling
                            genDefaultFragment(output)
                        } else {
                            f.withReader { r -> output << r }
                            output.flush()
                            iLog("JSP fragment written")
                        }
                    } else {
                        genDefaultFragment(output)
                    }
                }

                log("${genFiles.size()} file(s) generated")
                genFiles.each {
                    log("  - $it")
                }

                break
            default:
                logger.error("create $arg0 is not implemented")
                runner.help(arg0)
        }

    }

}

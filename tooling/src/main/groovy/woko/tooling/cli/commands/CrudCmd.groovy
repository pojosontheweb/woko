/*
 * Copyright 2001-2012 Remi Vankeisbelck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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
import woko.tooling.cli.Runner

import static woko.tooling.utils.AppUtils.*
import woko.tooling.cli.FacetCodeGenerator
import woko.facets.builtin.WokoFacets

class CrudCmd extends Command {

    CrudCmd(Runner runner, File projectDir, Logger logger) {
        super(runner, projectDir, logger,
                "crud",
                "Generate the CRUD facets for a given role (view/edit/save/delete)", "[<Entity> [<Role> [quiet]",
                """Command that generates the necessary facets for CRUD operations on a given target type :
  * view
  * edit
  * save
  * delete
""")
    }

    @Override
    void execute(List<String> args) {
        // args can be passed :
        String entityClassStr = getArgAt(args, 0)
        String role = getArgAt(args, 1)
        boolean quiet = getArgAt(args, 2) == "quiet"

        Class entityClass = null
        while (entityClass==null) {
            entityClassStr = modelFqcnFromSimpleName(entityClassStr != null ?
                            entityClassStr :
                            requiredAsk("type of the target entity"))
            try {
                entityClass = Class.forName(entityClassStr)
            } catch(ClassNotFoundException e) {
                iLog("ERROR: Class not found : $entityClassStr")
            }
        }

        role = role != null ?
                role :
                requiredAsk("specify the role")

        def facetNames = ["view"]
        boolean genEdit = false
        boolean genDelete = false
        if (quiet) {
            genEdit = true
            genDelete = true
        } else {
            genEdit = yesNoAsk("generate edit facet")
            genDelete = yesNoAsk("generate delete facet")
        }
        if (genEdit) {
            facetNames << "edit"
            facetNames << "save"
        }
        if (genDelete) {
            facetNames << "delete"
        }

        boolean useGroovy = askIfUseGroovy(quiet)

        String basePackage = getBaseFacetPackage()

        iLog(" ") // line sep
        iLog(" --- Summary ---")
        iLog(" ") // line sep
        iLog(" The following facet(s) are going to be generated :")
        facetNames.each {
            iLog("  - $it, $role, $entityClass ")
        }
        iLog(" ") // line sep
        def lang = useGroovy ? "Groovy" : "pure Java"
        iLog(" Facet written in  : $lang")
        iLog(" Facet source dir  : $projectDir.absolutePath/src/main/${useGroovy ? "groovy" : "java"}")
        iLog(" ") // line sep
        boolean doIt = quiet ? true : yesNoAsk("Is this OK ? Shall we generate all this (n to view gen sources only)")

        def genFiles = []
        facetNames.each { facetName ->
            // file generation
            def baseClass = WokoFacets.getDefaultImpl(facetName)
            def baseIntf = WokoFacets.getInterface(facetName)

            def facetClassName = "${basePackage}.$role.${capitalize(facetName)}${entityClass.simpleName}${capitalize(role)}"
            def facetFilePath = new FacetCodeGenerator(logger,projectDir,facetName,role,facetClassName).
                    setTargetObjectType(entityClass.getName()).
                    setBaseClass(baseClass).
                    setInterface(baseIntf).
                    setUseGroovy(useGroovy).
                    setDontGenerate(
                            !doIt
                    ).
                    generate()
            if (facetFilePath) {
                genFiles << facetFilePath
            }
        }

        println genFiles
    }

}

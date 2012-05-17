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

package woko.tooling.cli.commands

import woko.tooling.cli.Command
import net.sourceforge.jfacets.FacetDescriptor
import woko.tooling.utils.Logger
import woko.tooling.cli.Runner

class ListCmd extends Command {

    ListCmd(Runner runner) {
        super(
                runner,
                "list",
                "list facets or roles",
                "facets|roles",
                """Lists components of your application by using runtime information.
The command accepts one argument that can be  :
  * facets   : inits the facets of your app and lists them
  * roles    : lists all the roles defined in your application facets"""
        )
    }

    @Override
    void execute(List<String> args) {
        String arg0 = getArgAt(args,0)
        switch (arg0) {
            case "facets":
                def fdm = getFdm()
                def descriptors = fdm.descriptors
                logger.log("${descriptors.size()} facets found : ")
                def mkstr = { FacetDescriptor fd ->
                    return "$fd.name-$fd.profileId-$fd.targetObjectType-$fd.facetClass.name"
                }
                descriptors.sort { a,b ->
                    return mkstr(a) <=> mkstr(b)
                }.each { FacetDescriptor d ->
                    logger.log "  $d.name, $d.profileId, $d.targetObjectType.name, $d.facetClass.name"
                }
                break
            case "roles":
                def fdm = getFdm()
                def descriptors = fdm.descriptors
                def allRoles = []
                descriptors.each { FacetDescriptor d ->
                    def role = d.profileId
                    if (!allRoles.contains(role)) {
                        allRoles << role
                    }
                }
                logger.log("${allRoles.size()} role(s) used in faced keys :")
                allRoles.sort().each { r ->
                    logger.log "  $r"
                }
                break
            default:
                logger.error("invalid list command")
                runner.help("list")
        }


    }


}

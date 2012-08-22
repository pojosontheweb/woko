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
import woko.actions.WokoFacetBindingPolicyManager
import java.beans.PropertyDescriptor
import net.sourceforge.stripes.util.ReflectUtil
import java.lang.reflect.Method
import java.lang.reflect.Type
import java.lang.reflect.ParameterizedType
import woko.facets.ResolutionFacet
import net.sourceforge.stripes.util.ResolverUtil
import net.sourceforge.stripes.action.ActionBean
import java.lang.reflect.Modifier
import woko.actions.WokoActionBean
import net.sourceforge.stripes.action.ActionBeanContext

class ListCmd extends Command {

    ListCmd(Runner runner) {
        super(
                runner,
                "list",
                "list various application stuff",
                "facets|roles|bindings",
                """Lists components of your application by using runtime information.
The command accepts one argument that can be  :
  * facets   : inits the facets of your app and lists them
  * roles    : lists all the roles defined in your application facets
  * bindings : lists all possible HTTP request bindings on your facets and action beans"""
        )
    }

    @Override
    def execute(List<String> args) {
        String arg0 = getArgAt(args,0)
        switch (arg0) {
            case "facets":
                String arg1 = getArgAt(args, 1)
                def fdm
                if (arg1=="customClassLoader") {
                    fdm = getFdmCustomClassLoader() // TODO not sure this is useful : had been done for the widea plugin... remove ?
                } else {
                    fdm = getFdm()
                }
                def descriptors = fdm.descriptors
                logger.log("${descriptors.size()} facets found : ")
                def mkstr = { FacetDescriptor fd ->
                    return "$fd.name-$fd.profileId-$fd.targetObjectType-$fd.facetClass.name"
                }
                def sorted = []
                descriptors.sort { a,b ->
                    return mkstr(a) <=> mkstr(b)
                }.each { FacetDescriptor d ->
                    logger.log "  $d.name, $d.profileId, $d.targetObjectType.name, $d.facetClass.name"
                    sorted << d
                }
                return sorted
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
                def sorted = []
                allRoles.sort().each { r ->
                    logger.log "  $r"
                    sorted << r
                }
                return sorted
            case "bindings" :
                return listBindings()
            default:
                logger.error("invalid list command")
                return runner.help("list")
        }


    }

    private static def WRAPPER_TYPES = [
        String.class,
        Boolean.class,
        Character.class,
        Byte.class,
        Short.class,
        Integer.class,
        Long.class,
        Float.class,
        Double.class,
        Void.class
    ]

    public static boolean isWrapperType(Class<?> clazz)
    {
        return WRAPPER_TYPES.contains(clazz);
    }

    private static HashSet<Class<?>> EXCLUDED_TYPES = [
            ActionBeanContext.class,
            Class.class,
            MetaClass.class
    ]

    private static isExcluded(Class<?> clazz) {
        return EXCLUDED_TYPES.contains(clazz) || WokoFacetBindingPolicyManager.UNBINDABLE_CLASSES.contains(clazz)
    }


    def buildPathsTree(MANode node, Class<?> rootClass, WokoFacetBindingPolicyManager pm) {
        boolean canBind = false
        if (rootClass && !isExcluded(rootClass)) {

            // special handling for Object.class
            if (rootClass.equals(Object.class)) {
                // check if binding is allowed or not
                def n = new MANode(
                        parent:node,
                        type:Object.class,
                        name:"*")
                if (isBindingAllowed(n, pm)) {
                    node.children << n
                    return true; // can bind on Object but useless to recurse
                }
                return false; // protected by @StrictBinding
            }


            PropertyDescriptor[] pds = ReflectUtil.getPropertyDescriptors(rootClass);
            if (pds) {
                // sort properties for constant ordering
                pds.sort { pd1, pd2 ->
                    return pd1.name <=> pd2.name
                }.each { PropertyDescriptor pd ->
                    String propName = pd.name
                    Class<?> propType = pd.propertyType
                    boolean isList = false
                    if (propType && !isExcluded(propType)) {
                        boolean skip = false
                        // indexed props handling
                        if (propType.isArray()) {
                            propType = propType.getComponentType()
                            isList = true
                        } else if (List.class.isAssignableFrom(propType)) {
                            // grab the actual type of elements in the list if possible
                            Method readMethod = pd.readMethod
                            Type genReturnType = readMethod.genericReturnType
                            try {
                                ParameterizedType pt = (ParameterizedType)genReturnType
                                propType = pt.actualTypeArguments[0]
                                isList = true
                            } catch(Exception e) {
                                // not a parameterized List, binding impossible to determine anyway !
                                skip = true
                                // TODO add some kind of warning in there !
                            }
                        }
                        if (!skip) {
                            if (node.isSubjectToEndlessLoop(propType)) {
                                node.children << new MANode(
                                        parent:node,
                                        type:propType,
                                        name:propName,
                                        wouldRecurse: true,
                                        isList:isList)
                                // TODO not sure what to do with "canBind" here...
                            } else if (propType.isPrimitive() || isWrapperType(propType)) {
                                // primitive : check if it's settable
                                if (pd.writeMethod) {
                                    MANode n = new MANode(
                                            parent:node,
                                            type:propType,
                                            name:propName)
                                    if (isBindingAllowed(n,pm)) {
                                        node.children << n
                                        canBind = true
                                    }
                                }
                            } else {
                                MANode n = new MANode(
                                        parent:node,
                                        type:propType,
                                        name:propName,
                                        isList:isList)
                                // recurse and check if it binds below.
                                // don't add the node if no childs can bind
                                boolean childCanBind = isBindingAllowed(n, pm) && buildPathsTree(n, propType, pm)
                                if (childCanBind) {
                                    node.children << n
                                    canBind = true
                                }
                            }
                        }
                    }
                }
            }
        }
        return canBind
    }

    boolean isBindingAllowed(MANode node, WokoFacetBindingPolicyManager pm) {
        // special handling for ActionBean.context
        def parent = node.parent
        if (parent && ActionBean.class.isAssignableFrom(parent.type) && node.name=="context") {
            return false
        }
        // check by path
        String path = MANode.pathToString(node.absolutePath) { n -> n.name }
        return pm.isBindingAllowed(path)
    }

    def listBindings() {
        // load all resolution facets
        log("Listing bindings, facet packages :")
        computeFacetPackages().each { p ->
            log("  - $p")
        }
        log("")
        def descriptors = fdm.descriptors
        int totalPaths = 0
        // sort descriptors before looping for constant ordering
        descriptors.sort { d1,d2 ->
            d1.toString() <=> d2.toString()
        }.each { fd ->
            // find the target object type(s)
            if (ResolutionFacet.class.isAssignableFrom(fd.facetClass)) {

                // facet target type
                def type = fd.targetObjectType
                if (!type) {
                    type = Object.class
                }
                MANode root = new MANode(name:"object", type:type)
                WokoFacetBindingPolicyManager pm = WokoFacetBindingPolicyManager.getInstance(fd.facetClass, null)
                buildPathsTree(root, type, pm)
                int nbPaths = 0
                String prefix = "($fd.name,$fd.profileId,$type.name) [$fd.facetClass.name]"
                root.eachChildRecurse { MANode node ->
                    List<MANode> fullPath = node.absolutePath
                    String path = MANode.pathToString(fullPath)
                    log("$prefix $path")
                    nbPaths++
                }

                // facet direct fields
                MANode root2 = new MANode(name:"facet", type:fd.facetClass)
                buildPathsTree(root2, fd.facetClass, pm)
                root2.eachChildRecurse { MANode node ->
                    List<MANode> fullPath = node.absolutePath
                    String path = MANode.pathToString(fullPath)
                    log("$prefix $path")
                    nbPaths++
                }


                if (nbPaths) {
                    log("=> Found $nbPaths accessible binding(s) in facet $prefix\n")
                    totalPaths += nbPaths
                }
            }
        }

        // now scan for action beans
        def actionPackages = computeActionPackages().sort()
        log("Scanning for Action Beans in packages :")
        actionPackages.each { pkg ->
            log("  - $pkg")
        }
        log("")
        ResolverUtil<ActionBean> ru = new ResolverUtil<ActionBean>()
        String[] pkgs = actionPackages.toArray()
        ru.findImplementations(ActionBean.class, pkgs)
        ru.classes.sort { c1,c2 ->
            c1.name <=> c2.name
        }.each { actionClazz ->
            if (!Modifier.isAbstract(actionClazz.modifiers) &&
                (!WokoActionBean.class.equals(actionClazz))) {
                int nbPathsActionBean = 0
                MANode root = new MANode(name:null, type:actionClazz)
                WokoFacetBindingPolicyManager pm = WokoFacetBindingPolicyManager.getInstance(actionClazz, null)
                buildPathsTree(root, actionClazz, pm)
                String prefix = actionClazz.name
                root.eachChildRecurse { MANode node ->
                    List<MANode> fullPath = node.absolutePath
                    String path = MANode.pathToString(fullPath)
                    log("$prefix $path")
                    nbPathsActionBean++
                }
                if (nbPathsActionBean) {
                    log("=> Found $nbPathsActionBean accessible binding(s) in action bean $prefix\n")
                }
                totalPaths += nbPathsActionBean
            }
        }

        log("Found $totalPaths accessible bindings in the app.")
    }



}

class MANode {

    MANode parent
    List<MANode> children = []
    Class<?> type
    String name
    boolean wouldRecurse = false
    boolean isList = false

    boolean isSubjectToEndlessLoop(Class<?> type) {
        boolean res = false
        eachParentRecurse { p ->
            if (p.type==type) {
                res = true
                return false
            }
            return true
        }
        return res
    }

    String toString() {
        String s = "$name - $type"
        if (wouldRecurse) {
            s += "(*)"
        }
        return s
    }

    def eachChildRecurse(Closure c) {
        children.each { child->
            c(child)
            child.eachChildRecurse(c)
        }
    }

    def eachParentRecurse(Closure c) {
        def p = this
        if (p!=null) {
            if (c(p)) {
                if (p.parent) {
                    p.parent.eachParentRecurse(c)
                }
            }
        }
    }

    List<MANode> getAbsolutePath() {
        def res = []
        eachParentRecurse { p -> res << p }
        return res.reverse()
    }

    static String pathToString(List<MANode> path, Closure nameClosure) {
        StringBuilder res = new StringBuilder()
        int index = 0
        path.each { n ->
            if (n.name) {
                res << nameClosure(n)
                if (index<path.size()-1) {
                    res << "."
                }
            }
            index++
        }
        return res.toString()
    }

    static String pathToString(List<MANode> path) {
        pathToString(path) { n ->
            def s = n.name
            if (n.isList) {
                s += "[]"
            }
            return s
        }
    }

}


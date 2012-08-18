package woko.tooling.cli.commands

import woko.tooling.cli.Command
import woko.tooling.cli.Runner

import net.sourceforge.stripes.util.ReflectUtil
import java.beans.PropertyDescriptor
import woko.facets.ResolutionFacet

/**
 * Created by IntelliJ IDEA.
 * User: vankeisb
 * Date: 17/08/12
 * Time: 23:25
 * To change this template use File | Settings | File Templates.
 */
class MassAssignAuditCmd extends Command {

    private static final HashSet<Class<?>> WRAPPER_TYPES = getWrapperTypes();

    public static boolean isWrapperType(Class<?> clazz)
    {
        return WRAPPER_TYPES.contains(clazz);
    }

    private static HashSet<Class<?>> getWrapperTypes()
    {
        HashSet<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(String.class);
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        return ret;
    }

    private static HashSet<Class<?>> EXCLUDED_TYPES = [
            Class.class,
            MetaClass.class
    ]

    private static isExcluded(Class<?> clazz) {
        return EXCLUDED_TYPES.contains(clazz)
    }

    MassAssignAuditCmd(Runner runner) {
        super(
                runner,
                "sec-check",
                "performs security checks on the app",
                "mass-assign",
                """Checks security requirements  :
  * mass-assign   : provides a mass assignment report : tells you what paths can be bound"""
        )
    }

    def execute(List<String> args) {
        String arg0 = getArgAt(args,0)
        switch (arg0) {
            case "mass-assign": checkMassAssign(); break;
            default: checkMassAssign();
        }
    }

    private def buildPathsTree(MANode node, Class<?> rootClass) {
        boolean canBind = false
        if (rootClass && !isExcluded(rootClass)) {
            PropertyDescriptor[] pds = ReflectUtil.getPropertyDescriptors(rootClass);
            if (pds) {
                pds.each { PropertyDescriptor pd ->
                    String propName = pd.name
                    Class<?> propType = pd.propertyType
                    if (propType && !isExcluded(propType)) {
//                        if (propType.isArray()) {
//                            propType = propType.getComponentType()
//                        }
                        if (node.isSubjectToEndlessLoop(propType)) {
                            node.children << new MANode(
                                    parent:node,
                                    type:propType,
                                    name:propName + "!",
                                    wouldRecurse: true)
                            // TODO not sure what to do with "canBind" here...
                        } else if (propType.isPrimitive() || isWrapperType(propType)) {
                            // primitive : check if it's settable
                            if (pd.writeMethod) {
                                MANode n = new MANode(
                                        parent:node,
                                        type:propType,
                                        name:propName)
                                node.children << n
                                canBind = true
                            }
                        } else {
                            MANode n = new MANode(
                                    parent:node,
                                    type:propType,
                                    name:propName)
                            if (buildPathsTree(n, propType)) {
                                node.children << n
                                canBind = true
                            }
                        }
                    }
                }
            }
        }
        return canBind
    }

    def checkMassAssign() {
        // load all resolution facets
        def descriptors = fdm.descriptors
        int totalPaths = 0
        descriptors.each { fd ->
            // find the target object type(s)
            if (ResolutionFacet.class.isAssignableFrom(fd.facetClass)) {
                def type = fd.targetObjectType

                MANode root = new MANode(name:fd.name, type:type)
                buildPathsTree(root, type)

                int nbPaths = 0
                root.eachNodeRecurse { MANode node ->
                    if (!node.children) {
                        // leaf : print all path
                        log("  " + node.getAbsolutePath())
                        nbPaths++
                    }
                }

                if (nbPaths) {
                    log("=> Found $nbPaths accessible binding(s) in $fd")
                    totalPaths += nbPaths
                }
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

    boolean isSubjectToEndlessLoop(Class<?> type) {
        MANode n = this
        while (n) {
            if (n.type==type) {
                return true
            }
            n = n.parent
        }
        return false
    }

    String toString() {
        String s = "$name - $type"
        if (wouldRecurse) {
            s += "(*)"
        }
        return s
    }

    def eachNodeRecurse(Closure c) {
        children.each { child->
            c(child)
            child.eachNodeRecurse(c)
        }
    }

    String getAbsolutePath() {
        String s = ""
        MANode n = this
        while (n) {
            s = n.name + "/" + s
            n = n.parent
        }
        return s
    }
}
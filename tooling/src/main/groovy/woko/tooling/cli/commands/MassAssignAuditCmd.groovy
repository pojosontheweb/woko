package woko.tooling.cli.commands

import woko.tooling.cli.Command
import woko.tooling.cli.Runner

import net.sourceforge.stripes.util.ReflectUtil
import java.beans.PropertyDescriptor
import woko.facets.ResolutionFacet
import woko.tooling.utils.Logger
import java.lang.reflect.Method
import java.lang.reflect.Type
import java.lang.reflect.ParameterizedType
import net.sourceforge.stripes.action.StrictBinding
import net.sourceforge.stripes.action.StrictBinding.Policy
import woko.actions.WokoFacetBindingPolicyManager

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
        return EXCLUDED_TYPES.contains(clazz) || WokoFacetBindingPolicyManager.UNBINDABLE_CLASSES.contains(clazz)
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

    def buildPathsTree(MANode node, Class<?> rootClass, WokoFacetBindingPolicyManager pm) {
        boolean canBind = false
        if (rootClass && !isExcluded(rootClass)) {
            PropertyDescriptor[] pds = ReflectUtil.getPropertyDescriptors(rootClass);
            if (pds) {
                pds.each { PropertyDescriptor pd ->
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
        String path = MANode.pathToString(node.absolutePath) { n -> n.name }
        return pm.isBindingAllowed(path)
    }

    def checkMassAssign() {
        // load all resolution facets
        def descriptors = fdm.descriptors
        int totalPaths = 0
        descriptors.each { fd ->
            // find the target object type(s)
            if (ResolutionFacet.class.isAssignableFrom(fd.facetClass)) {

                // facet target type
                def type = fd.targetObjectType
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
                    log("=> Found $nbPaths accessible binding(s) in $prefix\n")
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
            res << nameClosure(n)
            if (index<path.size()-1) {
                res << "."
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


@StrictBinding(
defaultPolicy=Policy.ALLOW,
deny=[
    "object.dum.dummies"
]
)
class DummyFacetToHoldTheStrictBindingAnnotation {

    String foo


}

class TopDummy {

    DummyClass dum

}

class DummyClass {

    String dumS
    String dumS2
    List<Dummy2> dummies

}

class Dummy2 {

    Integer yup
    Dummy3 d3

}

class Dummy3 {
    String booyaka
}

class TestMe {

    public static void main(String[] args) {
//        MANode root = new MANode(name: "foo")
//        MANode child1 = new MANode(parent: root, name: "bar")
//        MANode child2 = new MANode(parent: root, name: "baz")
//        root.children << child1
//        root.children << child2
//        println MANode.pathToString(child2.absolutePath)

        Writer out = new PrintWriter(System.out)
        Logger logger = new Logger(out)
        Runner runner = new Runner(logger, new File("/Users/vankeisb/projects/msm"))
        MassAssignAuditCmd cmd = new MassAssignAuditCmd(runner)
        MANode node = new MANode(name:"object", type:TopDummy.class)
        WokoFacetBindingPolicyManager pm = WokoFacetBindingPolicyManager.getInstance(DummyFacetToHoldTheStrictBindingAnnotation.class, null)
        cmd.buildPathsTree(node, TopDummy.class, pm)
        println node
        node.eachChildRecurse { n ->
            println MANode.pathToString(n.absolutePath)
        }
    }

}
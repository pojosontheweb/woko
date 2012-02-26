package woko.tooling.cli

import woko.tooling.utils.Logger
import groovy.text.GStringTemplateEngine

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
        def pc = extractPkgAndClazz(facetClassName)
        binding["facetPackage"] = pc.pkg
        binding["facetClassName"] = pc.clazz
        binding["targetTypeStr"] = ""
        binding["baseClassStr"] = ""
        binding["interfaceStr"] = ""
        binding["classBody"] = ""
    }

    private def extractPkgAndClazz(String fqcn) {
        int i = fqcn.lastIndexOf(".")
        if (i!=-1) {
            return [pkg:fqcn[0..i-1], clazz:fqcn[i+1..-1]]
        }
        return [pkg:'',clazz:fqcn]
    }

    FacetCodeGenerator setTargetObjectType(String targetType) {
        if (targetType && targetType!="Object" && targetType!="java.lang.Object") {
            def pc = extractPkgAndClazz(targetType)
            binding["targetTypeStr"] = ", targetObjectType=${pc.clazz}.class"
            imports << targetType
        }
        this
    }

    FacetCodeGenerator setBaseClass(Class<?> baseClass) {
        if (baseClass) {
            def pc = extractPkgAndClazz(baseClass.name)
            binding["baseClassStr"] = " extends $pc.clazz"
            imports << baseClass.name
        }
        this
    }

    FacetCodeGenerator setInterface(Class<?> intf) {
        if (intf) {
            def pc = extractPkgAndClazz(intf.name)
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

    void generate() {
        if (dontGenerate) {
            logger.log("Dry run, no files will be written. The source is simply printed here :")
            logger.log(" ")
            logger.log(" ")
            generate(logger.writer)
            logger.log(" ")
            logger.log(" ")
        } else {
            String s = facetClassName.replaceAll("\\.", "/")
            String pathToFile = "$baseDir.absolutePath/src/main/${useGroovy ? "groovy" : "java"}/${s}.${useGroovy ? "groovy" : "java"}"
            logger.log("Generating file $pathToFile")
            new File(pathToFile).withWriter { w ->
                generate(w)
            }
        }
        logger.log("Facet source generated.")
    }

    FacetCodeGenerator setDontGenerate(boolean dontGen) {
        this.dontGenerate = dontGen
        this
    }
}

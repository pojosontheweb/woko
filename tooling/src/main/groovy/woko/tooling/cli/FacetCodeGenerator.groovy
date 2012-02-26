package woko.tooling.cli

import woko.tooling.utils.Logger
import groovy.text.GStringTemplateEngine

class FacetCodeGenerator {

    private final Logger logger
    private final File baseDir

    private final def binding = [:]
    private boolean useGroovy = false

    FacetCodeGenerator(Logger logger, File baseDir, String name, String role, String facetClassName) {
        this.logger = logger
        this.baseDir = baseDir
        binding["name"] = name
        binding["role"] = role
        int i = facetClassName.lastIndexOf(".")
        if (i!=-1) {
            binding["facetPackage"] = facetClassName[0..i-1]
            binding["facetClassName"] = facetClassName[i+1..-1]
        } else {
            binding["facetClassName"] = facetClassName
            binding["facetPackage"] = ""
        }
    }

    FacetCodeGenerator setTargetObjectType(String targetType) {
        if (targetType) {
            binding["targetObjectType"] = targetType
        }
        this
    }

    FacetCodeGenerator setBaseClass(Class<?> baseClass) {
        if (baseClass) {
            binding["baseClass"] = baseClass
        }
        this
    }

    FacetCodeGenerator setInterface(Class<?> intf) {
        if (intf) {
            binding["intf"] = intf
        }
        this
    }

    FacetCodeGenerator setFragmentPath(String path) {
        if (path) {
            binding["fragmentPath"] = path
        }
        this
    }

    FacetCodeGenerator setUseGroovy(boolean useGroovy) {
        this.useGroovy = useGroovy
        this
    }

    void generate(Writer out) {
        def engine = new GStringTemplateEngine()
        def tpl = useGroovy ? "facet-groovy.template" : "facet-java.template"
        engine.
          createTemplate(getClass().getResource(tpl)).
          make(binding).
          writeTo(out)
    }

    void generate() {
        System.out.withWriter { w ->
            generate(w)
        }
    }
}

package woko.push;

import net.sourceforge.jfacets.FacetDescriptor;
import org.codehaus.groovy.control.CompilationFailedException;

import java.util.Collections;
import java.util.List;

public class PushedSourceResult {

    private final String source;
    private final Class<?> facetClass;
    private final List<FacetDescriptor> facetDescriptors;
    private final CompilationFailedException compilationException;

    public PushedSourceResult(String source, Class<?> facetClass, List<FacetDescriptor> facetDescriptors) {
        this(source, facetClass, facetDescriptors, null);
    }

    public PushedSourceResult(String source, CompilationFailedException e) {
        this(source, null, null, e);
    }

    private PushedSourceResult(
      String source,
      Class<?> facetClass,
      List<FacetDescriptor> facetDescriptors,
      CompilationFailedException e) {
        this.source = source;
        this.facetClass = facetClass;
        this.facetDescriptors = Collections.unmodifiableList(
          facetDescriptors==null ? Collections.<FacetDescriptor>emptyList() : facetDescriptors);
        this.compilationException = e;
    }

    public String getSource() {
        return source;
    }

    public Class<?> getFacetClass() {
        return facetClass;
    }

    public List<FacetDescriptor> getFacetDescriptors() {
        return facetDescriptors;
    }

    public CompilationFailedException getCompilationException() {
        return compilationException;
    }
}

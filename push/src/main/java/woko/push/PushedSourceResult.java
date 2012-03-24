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

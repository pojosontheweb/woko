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

import groovy.lang.GroovyClassLoader;
import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.jfacets.annotations.FacetKeyList;
import org.codehaus.groovy.control.CompilationFailedException;
import woko.util.WLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PushFacetDescriptorManager implements IFacetDescriptorManager {

    private final IFacetDescriptorManager delegate;

    private List<FacetDescriptor> pushedDescriptors = new ArrayList<FacetDescriptor>();

    private static final WLogger logger = WLogger.getLogger(PushFacetDescriptorManager.class);

    public PushFacetDescriptorManager(IFacetDescriptorManager delegate) {
        logger.info("Using Push with delegate : " + delegate);
        this.delegate = delegate;
    }

    public FacetDescriptor[] getDescriptors() {
        List<FacetDescriptor> all = new ArrayList<FacetDescriptor>();
        all.addAll(Arrays.asList(delegate.getDescriptors()));
        // add / replace with pushed descriptors
        List<FacetDescriptor> toBeRemoved = new ArrayList<FacetDescriptor>();
        for (FacetDescriptor fd : pushedDescriptors) {
            FacetDescriptor fdDelegate = findDescriptorInList(fd, all);
            if (fdDelegate != null) {
                toBeRemoved.add(fdDelegate);
            }
            all.add(fd);
        }
        for (FacetDescriptor fd : toBeRemoved) {
            all.remove(fd);
        }
        FacetDescriptor[] result = new FacetDescriptor[all.size()];
        return all.toArray(result);
    }


    private boolean equals(String name, String profileId, Class<?> targetObjectType, FacetDescriptor fd) {
        return fd.getName().equals(name) &&
          fd.getProfileId().equals(profileId) &&
          fd.getTargetObjectType().equals(targetObjectType);
    }

    private boolean equals(FacetDescriptor fd1, FacetDescriptor fd2) {
        return equals(fd1.getName(), fd1.getProfileId(), fd1.getTargetObjectType(), fd2);
    }

    private FacetDescriptor findDescriptorInList(FacetDescriptor fd, List<FacetDescriptor> descriptors) {
        for (FacetDescriptor it : descriptors) {
            if (equals(fd, it)) {
                return it;
            }
        }
        return null;
    }

    public FacetDescriptor getDescriptor(String name, String profileId, Class targetObjectType) {
        // lookup pushed descriptors first
        for (FacetDescriptor fd : pushedDescriptors) {
            if (equals(name, profileId, targetObjectType, fd)) {
                return fd;
            }
        }
        // fallback to delegate
        return delegate.getDescriptor(name, profileId, targetObjectType);
    }

    public PushResult reload(List<String> sources) {
        List<PushedSourceResult> pushedSourceResults = new ArrayList<PushedSourceResult>();
        List<FacetDescriptor> reloadedDescriptors = new ArrayList<FacetDescriptor>();
        if (sources != null && sources.size() > 0) {
            logger.info("Reloading with " + sources.size() + " source(s)");
            GroovyClassLoader cl = new GroovyClassLoader();
            for (String source : sources) {
                try {
                    Class<?> clazz = cl.parseClass(source);
                    // class obtained, grab @FacetKey metadata
                    List<FacetDescriptor> facetDescriptors =  createDescriptors(clazz);
                    if (facetDescriptors.size()==0) {
                        logger.warn("No @FacetKey for parsed class " + clazz + ", source :\n" + source + "\n");
                    } else {
                        reloadedDescriptors.addAll(facetDescriptors);
                    }
                    pushedSourceResults.add(new PushedSourceResult(source, clazz, facetDescriptors));
                } catch (CompilationFailedException e) {
                    logger.error("Unable to parse class for source :\n" + source + "\n", e);
                    pushedSourceResults.add(new PushedSourceResult(source, e));
                }
            }
        }
        logger.info("Loaded " + reloadedDescriptors.size() + " descriptors");
        pushedDescriptors = reloadedDescriptors;
        return new PushResult(pushedSourceResults);
    }

    private List<FacetDescriptor> createDescriptors(Class<?> facetClass) {
        List<FacetDescriptor> result = new ArrayList<FacetDescriptor>();
        FacetKeyList fkl = facetClass.getAnnotation(FacetKeyList.class);
        if (fkl != null) {
            FacetKey[] facetKeys = fkl.keys();
            for (FacetKey facetKey : facetKeys) {
                createDescriptorForAnnotation(facetKey, facetClass, result);
            }

        } else {
            // no multiple key, try single one...
            FacetKey annot = facetClass.getAnnotation(FacetKey.class);
            if (annot != null) {
                createDescriptorForAnnotation(annot, facetClass, result);
            } else {
                logger.warn("Skipped class " + facetClass + ", does not have the @FacetKey annot");
            }
        }
        return result;
    }

    private void createDescriptorForAnnotation(FacetKey annot, Class<?> facetClass, List<FacetDescriptor> result) {
        // annotated, create and add descriptor
        String name = annot.name();
        String profileId = annot.profileId();
        Class<?> targetObjectType = annot.targetObjectType();
        if (name == null || profileId == null || targetObjectType == null) {
            logger.warn("name, profileId or targetObjectType not found in @FacetKey annotation for class " + facetClass);
        }
        FacetDescriptor descriptor = new FacetDescriptor();
        descriptor.setName(name);
        descriptor.setProfileId(profileId);
        descriptor.setTargetObjectType(targetObjectType);
        descriptor.setFacetClass(facetClass);
        logger.info("Created and added descriptor " + descriptor + " for facet class " + facetClass);
        result.add(descriptor);
    }


}

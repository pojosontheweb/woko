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

package woko.facets.builtin.push;

import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.Woko;
import woko.facets.BaseResolutionFacet;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.StreamingResolution;
import woko.push.PushFacetDescriptorManager;
import woko.push.PushResult;
import woko.push.PushedSourceResult;
import woko.util.WLogger;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@FacetKey(name = "push", profileId = "developer")
public class PushFacet extends BaseResolutionFacet {

    private static final WLogger logger = WLogger.getLogger(PushFacet.class);

    private List<String> sources = new ArrayList<String>();

    public List<String> getSources() {
        return sources;
    }

    public Resolution getResolution(ActionBeanContext abc) {
        StringBuilder sb = new StringBuilder();
        if (sources.size()>0) {
            Woko<?,?,?,?> woko = getFacetContext().getWoko();
            IFacetDescriptorManager fdm = woko.getFacetDescriptorManager();
            if (!(fdm instanceof PushFacetDescriptorManager)) {
                String msg = "Trying to push but facet descriptor manager ain't a PushFacetDescriptorManager !";
                logger.error(msg);
                throw new IllegalStateException(msg);
            }
            PushFacetDescriptorManager pfdm = (PushFacetDescriptorManager)fdm;
            HttpServletRequest request = getFacetContext().getRequest();
            String username = woko.getUsername(request);
            String remoteHost = request.getRemoteHost();
            logger.warn("Push requested by user '" + username + "' from remote host '" + remoteHost + "'...");
            logger.info("Pushing " + sources.size() + " source files");
            PushResult res = pfdm.reload(sources);
            logger.warn("user '" + username + "' pushed from remote host '" + remoteHost + "'");
            List<PushedSourceResult> pushedSourceResults = res.getPushedSourceResults();
            if (pushedSourceResults.size()==0) {
                sb.append("Nothing pushed. No sources posted ???");
            } else {
                sb.append(Integer.toString(pushedSourceResults.size())).
                  append(" source(s) pushed and compiled :\n");
                for (PushedSourceResult pushedSourceResult : pushedSourceResults) {
                    Class<?> facetClass = pushedSourceResult.getFacetClass();
                    if (facetClass!=null) {
                        sb.append("  - ").
                          append(facetClass.getName()).
                          append(" => ");
                        List<FacetDescriptor> facetDescriptors = pushedSourceResult.getFacetDescriptors();
                        if (facetDescriptors!=null && facetDescriptors.size()>0) {
                            for (FacetDescriptor fd : facetDescriptors) {
                                sb.append("(").
                                  append(fd.getName()).
                                  append(",").
                                  append(fd.getProfileId()).
                                  append(",").
                                  append(fd.getTargetObjectType().getName()).
                                  append(") ");
                            }
                        } else {
                            sb.append("No descriptor (not a facet class ???)");
                        }
                        sb.append("\n");
                    } else {
                        sb.append("  - Compilation error :\n").
                          append("Source :\n").
                          append(pushedSourceResult.getSource()).
                          append("\nCompilation errors :").
                          append(pushedSourceResult.getCompilationException()).
                          append("\n");
                    }
                }
            }
        } else {
            sb.append("Nothing pushed (no sources posted).");
        }
        return new StreamingResolution("text/plain", sb.toString());
    }

}

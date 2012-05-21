/*
 * Copyright 2001-2012 Remi Vankeisbelck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package woko.idea;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.*;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.GlobalSearchScope;
import net.sourceforge.jfacets.FacetDescriptor;
import org.jetbrains.annotations.NotNull;
import woko.tooling.cli.Runner;
import woko.tooling.utils.AppUtils;
import woko.tooling.utils.Logger;
import woko.tooling.utils.PomHelper;

import java.io.File;
import java.io.StringWriter;
import java.util.*;

public class WokoProjectComponent implements ProjectComponent {

    private final Project project;
    private JavaPsiFacade psiFacade;
    private GlobalSearchScope projectScope;

    private PushFacetsDialogWrapper pushDialog = null;

    private List<FacetDescriptor> facetDescriptors = Collections.emptyList();
    private Map<String,Long> refreshStamps = Collections.emptyMap();

    public WokoProjectComponent(Project project) {
        this.project = project;
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }

    @NotNull
    public String getComponentName() {
        return "WokoProjectComponent";
    }

    public void projectOpened() {
        psiFacade = JavaPsiFacade.getInstance(project);
        projectScope = GlobalSearchScope.projectScope(project);
    }

    public void projectClosed() {
        // called when project is being closed
    }

    public boolean openClassInEditor(String fqcn) {
        if (fqcn==null) {
            return false;
        }
        PsiClass c = getPsiClass(fqcn);
        if (c!=null) {
            c.getContainingFile().navigate(true);
            return true;
        }
        return false;
    }

    public void refresh() {
        // invoke runner to grab the facets in the project
        VirtualFile baseDir = project.getBaseDir();
        if (baseDir==null) {
            facetDescriptors = Collections.emptyList();
        } else {
            File projectRootDir = new File(baseDir.getPath());
            StringWriter sw = new StringWriter();
            Logger logger = new Logger(sw);
            Runner runner = new Runner(logger, projectRootDir);
            try {
                Object result = runner.invokeCommand("list", "facets", "customClassLoader");
                if (result instanceof List) {
                    facetDescriptors = (List<FacetDescriptor>)result;
                }
            } catch(Exception e) {
                facetDescriptors = Collections.emptyList();
                // TODO handle errors
            }
        }
        // init refresh stamps
        Map<String,Long> newStamps = new HashMap<String, Long>();
        for (FacetDescriptor fd : facetDescriptors) {
            String fqcn = fd.getFacetClass().getName();
            // keep old stamp if any
            Long oldStamp = refreshStamps.get(fqcn);
            if (oldStamp==null) {
                PsiFile f = getPsiFile(fqcn);
                if (f!=null) {
                    newStamps.put(fqcn, f.getModificationStamp());
                }
            } else {
                newStamps.put(fqcn, oldStamp);
            }
        }
        refreshStamps = newStamps;
    }

    public List<FacetDescriptor> getFacetDescriptors() {
        return facetDescriptors;
    }

    public boolean push(String url, String username, String password) {
        // retrieve the facet sources from modified facets
        List<String> facetSources = new ArrayList<String>();
        for (FacetDescriptor fd : facetDescriptors) {
            if (isModifiedSinceLastRefresh(fd)) {
                String fqcn = fd.getFacetClass().getName();
                PsiClass psiClass = getPsiClass(fqcn);
                facetSources.add(psiClass.getContainingFile().getText());
            }
        }

        // push all this !
        PomHelper pomHelper = AppUtils.getPomHelper(new File(project.getBaseDir().getPath()));
        StringWriter sw = new StringWriter();
        Logger logger = new Logger(sw);
        AppUtils.pushFacetSources(pomHelper, logger, url, username, password, facetSources);
        return true;
    }

    public boolean isModifiedSinceLastRefresh(FacetDescriptor fd) {
        Long lastRefreshStamp = getLastRefreshStamp(fd);
        if (lastRefreshStamp!=null) {
            PsiFile f = getPsiFile(fd.getFacetClass().getName());
            if (f!=null) {
                long modifStamp = f.getModificationStamp();
                return modifStamp!=lastRefreshStamp;
            }
        }
        return false;
    }

    public PsiClass getPsiClass(String fqcn) {
        return psiFacade.findClass(fqcn, projectScope);
    }

    public PsiFile getPsiFile(String fqcn) {
        PsiClass pc = getPsiClass(fqcn);
        if (pc!=null) {
            return pc.getContainingFile();
        }
        return null;
    }

    public void openPushDialog() {
        if (pushDialog==null) {
            pushDialog = new PushFacetsDialogWrapper(project);
            pushDialog.setTitle("Push facets...");
        }
        pushDialog.pack();
        pushDialog.show();
    }


    public Long getLastRefreshStamp(FacetDescriptor fd) {
        return refreshStamps.get(fd.getFacetClass().getName());
    }
}


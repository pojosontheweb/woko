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
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiImmediateClassType;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;
import java.util.*;

public class WokoProjectComponent implements ProjectComponent {

    private final Project project;
    private JavaPsiFacade psiFacade;
    private GlobalSearchScope projectScope;
    private List<WideaFacetDescriptor> facetDescriptors = Collections.emptyList();

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
        facetDescriptors = scanForFacets();
    }

    public void projectClosed() {
        // called when project is being closed
        facetDescriptors = Collections.emptyList();
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

    public List<WideaFacetDescriptor> getFacetDescriptors() {
        return facetDescriptors;
    }

    private List<WideaFacetDescriptor> scanForFacets() {
        // scan configured package for classes annotated with @FacetKey[List]
        List<String> packageNamesFromConfig = Arrays.asList("woko.facets.builtin"); // TODO get pkgs from web.xml
        List<WideaFacetDescriptor> scannedDescriptors = new ArrayList<WideaFacetDescriptor>();
        for (String pkgName : packageNamesFromConfig) {
            PsiPackage psiPkg = psiFacade.findPackage(pkgName);
            if (psiPkg!=null) {
                PsiClass[] psiClasses = psiPkg.getClasses();
                for (PsiClass psiClass : psiClasses) {
                    List<WideaFacetDescriptor> descriptors = getFacetDescriptorsForClass(psiClass);
                    if (descriptors!=null) {
                        scannedDescriptors.addAll(descriptors);
                    }
                }
            }
        }
        return scannedDescriptors;
    }

    private List<WideaFacetDescriptor> getFacetDescriptorsForClass(PsiClass psiFacetClass) {
        PsiModifierList modList = psiFacetClass.getModifierList();
        List<WideaFacetDescriptor> res = new ArrayList<WideaFacetDescriptor>();
        if (modList!=null) {
            PsiAnnotation psiFacetKey = getAnnotation(psiFacetClass, "net.sourceforge.jfacets.annotations.FacetKey");
            if (psiFacetKey!=null) {
                res.add(createDescriptorForKey(psiFacetClass, psiFacetKey));
            } else {
                PsiAnnotation psiFacetKeyList = getAnnotation(psiFacetClass, "net.sourceforge.jfacets.annotations.FacetKeyList");
                if (psiFacetKeyList!=null) {
                    res.addAll(createDescriptorsForKeyList(psiFacetClass, psiFacetKeyList));
                }
            }
        }
        return res;
    }

    private List<WideaFacetDescriptor> createDescriptorsForKeyList(PsiClass psiFacetClass, PsiAnnotation psiFacetKeyList) {
        PsiNameValuePair[] nvps = psiFacetKeyList.getParameterList().getAttributes();
        List<WideaFacetDescriptor> res = new ArrayList<WideaFacetDescriptor>();
        if (nvps.length==1) {
            PsiNameValuePair nvp = nvps[0];
            String name = nvp.getName();
            if (name!=null && name.equals("keys")) {
                PsiArrayInitializerMemberValue v = (PsiArrayInitializerMemberValue)nvp.getValue();
                if (v!=null) {
                    PsiAnnotationMemberValue[] keys = v.getInitializers();
                    for (PsiAnnotationMemberValue key : keys) {
                        PsiAnnotation a = (PsiAnnotation)key;
                        res.add(createDescriptorForKey(psiFacetClass, a));
                    }

                }
            }
        }
        return res;
    }

    private String getNvpValueText(PsiNameValuePair nvp) {
        PsiAnnotationMemberValue pv = nvp.getValue();
        if (pv instanceof PsiLiteralExpression) {
            return (String)((PsiLiteralExpression)pv).getValue();
        }
        return null;
    }

    private WideaFacetDescriptor createDescriptorForKey(PsiClass psiFacetClass, PsiAnnotation psiFacetKey) {
        PsiNameValuePair[] nvps = psiFacetKey.getParameterList().getAttributes();
        String name = null;
        String profileId = null;
        String targetObjectType = null;
        for (PsiNameValuePair nvp : nvps) {
            String pName = nvp.getName();
            if (pName!=null) {
                if (pName.equals("name")) {
                    name = getNvpValueText(nvp);
                } else if (pName.equals("profileId")) {
                    profileId = getNvpValueText(nvp);
                } else if (pName.equals("targetObjectType")) {
                    PsiAnnotationMemberValue pv = nvp.getValue();
                    if (pv instanceof PsiClassObjectAccessExpression) {
                        PsiClassObjectAccessExpression cae = (PsiClassObjectAccessExpression)pv;
                        PsiType type = cae.getType();
                        if (type instanceof PsiImmediateClassType) {
                            PsiImmediateClassType ict = (PsiImmediateClassType)type;
                            PsiType[] parameters = ict.getParameters();
                            if (parameters.length==1) {
                                targetObjectType = parameters[0].getCanonicalText();
                            }
                        }
                    }
                }
            }
        }
        String facetClassName = psiFacetClass.getQualifiedName();
        if (name!=null && profileId!=null && targetObjectType!=null && facetClassName!=null) {
            return new WideaFacetDescriptor(name, profileId, targetObjectType, facetClassName);
        }
        return null;
    }

    public PsiAnnotation getAnnotation(PsiClass psiClass, String annotFqcn) {
        PsiModifierList modifierList = psiClass.getModifierList();
        if (modifierList==null) {
            return null;
        }
        PsiAnnotation[] annots = modifierList.getAnnotations();
        for (PsiAnnotation a : annots) {
            String qn = a.getQualifiedName();
            if (qn!=null) {
                if (qn.equals(annotFqcn)) {
                    return a;
                }
            }
        }
        return null;
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

}


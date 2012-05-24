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
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.*;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiImmediateClassType;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.groovy.lang.psi.GrReferenceElement;
import org.jetbrains.plugins.groovy.lang.psi.api.GroovyResolveResult;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrReferenceExpression;
import woko.tooling.utils.AppUtils;
import woko.tooling.utils.Logger;
import woko.tooling.utils.PomHelper;

import java.io.File;
import java.io.StringWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WokoProjectComponent implements ProjectComponent {

    private final Project project;
    private JavaPsiFacade psiFacade;
    private GlobalSearchScope projectScope;
    private List<WideaFacetDescriptor> facetDescriptors = Collections.emptyList();
    private Map<String,WideaFacetDescriptor> filesAndDescriptors = Collections.emptyMap();
    private List<String> packagesFromConfig = Collections.emptyList();
    private MyVfsListener vfsListener = new MyVfsListener();

    private WokoToolWindow toolWindow = new WokoToolWindow();
    private PushServerInfoDialog pushDialog;

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

    class MyVfsListener extends VirtualFileAdapter {
        @Override
        public void contentsChanged(VirtualFileEvent event) {
            VirtualFile vf = event.getFile();
            String path = vf.getPath();
            refresh();
        }
    }

    public WokoToolWindow getToolWindow() {
        return toolWindow;
    }

    public void projectOpened() {
        psiFacade = JavaPsiFacade.getInstance(project);
        projectScope = GlobalSearchScope.projectScope(project);
        // register project file observer
        VirtualFileSystem vfs = project.getBaseDir().getFileSystem();
        vfs.addVirtualFileListener(vfsListener);
        // init tool window
        toolWindow.init(project);
    }

    public void projectClosed() {
        // called when project is being closed
        facetDescriptors = Collections.emptyList();
        VirtualFileSystem vfs = project.getBaseDir().getFileSystem();
        vfs.removeVirtualFileListener(vfsListener);
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

    public void refresh() {
        VirtualFile baseDir = project.getBaseDir();
        if (baseDir!=null) {
            // grab packages from web.xml
            VirtualFile f = baseDir.findFileByRelativePath("src/main/webapp/WEB-INF/web.xml");
            if (f!=null) {
                PsiFile file = PsiManager.getInstance(project).findFile(f);
                if (file != null && file instanceof XmlFile) {
                    XmlFile xmlFile = (XmlFile)file;
                    XmlDocument doc = xmlFile.getDocument();
                    XmlTag[] tags = doc.getRootTag().getSubTags();
                    List<String> pkgsFromConfig = new ArrayList<String>();
                    for (XmlTag tag : tags) {
                        if (tag.getName().equals("context-param")) {
                            String pName = tag.getSubTagText("param-name");
                            if (pName!=null && pName.equals("Woko.Facet.Packages")) {
                                String packagesStr = tag.getSubTagText("param-value");
                                if (packagesStr!=null) {
                                    pkgsFromConfig.addAll(extractPackagesList(packagesStr));
                                }
                            }
                        }
                    }
                    // add default Woko packages
                    pkgsFromConfig.add("facets");
                    pkgsFromConfig.add("woko.facets.builtin");
                    packagesFromConfig = pkgsFromConfig;
                    // scan
                    List<WideaFacetDescriptor> descriptors = new ArrayList<WideaFacetDescriptor>();
                    Map<String,WideaFacetDescriptor> filesDescriptors = new HashMap<String, WideaFacetDescriptor>();
                    scanForFacets(pkgsFromConfig, descriptors, filesDescriptors);
                    // update fields
                    facetDescriptors = descriptors;
                    filesAndDescriptors = filesDescriptors;
                }
            }
        } else {
            facetDescriptors = Collections.emptyList();
            filesAndDescriptors = Collections.emptyMap();
        }
        // fire refresh for the tool window's table model
        toolWindow.refreshTable();
    }

    private void scanForFacets(
            List<String> packageNamesFromConfig,
            List<WideaFacetDescriptor> scannedDescriptors,
            Map<String,WideaFacetDescriptor> filesDescriptors) {
        // scan configured package for classes annotated with @FacetKey[List]
        for (String pkgName : packageNamesFromConfig) {
            PsiPackage psiPkg = psiFacade.findPackage(pkgName);
            if (psiPkg!=null) {
                scanForFacetsRecursive(psiPkg, scannedDescriptors, filesDescriptors);
            }
        }
    }

    private void scanForFacetsRecursive(
            PsiPackage psiPkg,
            List<WideaFacetDescriptor> descriptors,
            Map<String,WideaFacetDescriptor> filesDescriptors) {
        // scan classes in package
        PsiClass[] psiClasses = psiPkg.getClasses();
        for (PsiClass psiClass : psiClasses) {
            List<WideaFacetDescriptor> classDescriptors = getFacetDescriptorsForClass(psiClass);
            if (classDescriptors!=null) {
                // we need to check if a descriptor already exists in previously scanned
                // packages (re-implement JFacets' "first scanned wins" policy)
                for (WideaFacetDescriptor fd : classDescriptors) {
                    if (fd!=null && !descriptors.contains(fd)) {
                        descriptors.add(fd);
                        // set the files/descriptors entry
                        PsiFile containingFile = psiClass.getContainingFile();
                        VirtualFile vf = containingFile.getVirtualFile();
                        if (vf!=null) {
                            String absolutePath = vf.getPath();
                            filesDescriptors.put(absolutePath, fd);
                        }
                    }
                }
            }
        }
        // recurse in sub-packages
        PsiPackage[] subPackages = psiPkg.getSubPackages();
        for (PsiPackage subPackage : subPackages) {
            scanForFacetsRecursive(subPackage, descriptors, filesDescriptors);
        }
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
                PsiAnnotationMemberValue mv = nvp.getValue();
                if (mv instanceof PsiArrayInitializerMemberValue) {
                    PsiArrayInitializerMemberValue v = (PsiArrayInitializerMemberValue)nvp.getValue();
                    if (v!=null) {
                        PsiAnnotationMemberValue[] keys = v.getInitializers();
                        for (PsiAnnotationMemberValue key : keys) {
                            PsiAnnotation a = (PsiAnnotation)key;
                            res.add(createDescriptorForKey(psiFacetClass, a));
                        }
                    }
                } else if (mv!=null) {
                    PsiElement[] children = mv.getChildren();
                    for (PsiElement child : children) {
                        if (child instanceof PsiAnnotation) {
                            res.add(createDescriptorForKey(psiFacetClass, (PsiAnnotation)child));
                        }
                    }
                }
            }
        }
        return res;
    }

    private String unquote(String text) {
        return text!=null ? text.replace("\"", "") : null;
    }

    private String getValueFromResolveResult(ResolveResult rr) {
        PsiElement elem = rr.getElement();
        if (elem instanceof PsiField) {
            PsiField pf = (PsiField)elem;
            PsiExpression initializer = pf.getInitializer();
            if (initializer!=null) {
                return unquote(initializer.getText());
            }
        }
        return null;
    }

    private String getNvpValueAsText(PsiAnnotationMemberValue pv) {
        if (pv!=null) {
            if (pv instanceof GrReferenceElement<?>) {
                GrReferenceElement<?> re = (GrReferenceElement<?>)pv;
                GroovyResolveResult rr = re.advancedResolve();
                return getValueFromResolveResult(rr);
            } else if (pv instanceof PsiReferenceExpression) {
                PsiReferenceExpression re = (PsiReferenceExpression)pv;
                JavaResolveResult rr = re.advancedResolve(true);
                return getValueFromResolveResult(rr);
            } else {
                return unquote(pv.getText());
            }
        }
        return null;
    }

    private WideaFacetDescriptor createDescriptorForKey(PsiClass psiFacetClass, PsiAnnotation psiFacetKey) {
        String name = getNvpValueAsText(psiFacetKey.findAttributeValue("name"));
        String profileId = getNvpValueAsText(psiFacetKey.findAttributeValue("profileId"));

        String targetObjectType = null;
        PsiAnnotationMemberValue pv = psiFacetKey.findAttributeValue("targetObjectType");
        PsiType classType = null;
        if (pv instanceof PsiClassObjectAccessExpression) {
            PsiClassObjectAccessExpression cae = (PsiClassObjectAccessExpression)pv;
            classType = cae.getType();
        } else if (pv instanceof GrReferenceExpression) {
            GrReferenceExpression refExpr = (GrReferenceExpression)pv;
            classType = refExpr.getNominalType();
        } else if (pv instanceof PsiLiteralExpression) {
            targetObjectType = "UNSUPPORTED YET!";
        }
        if (classType instanceof PsiImmediateClassType) {
            PsiImmediateClassType ict = (PsiImmediateClassType)classType;
            PsiType[] parameters = ict.getParameters();
            if (parameters.length==1) {
                targetObjectType = parameters[0].getCanonicalText();
            }
        }

        String facetClassName = psiFacetClass.getQualifiedName();
        targetObjectType = targetObjectType==null ? "java.lang.Object" : targetObjectType;
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

    public static List<String> extractPackagesList(String packagesStr) {
        String[] pkgNamesArr = packagesStr.
                replace('\n', ',').
                replace(' ', ',').
                split(",");
        List<String> pkgNames = new ArrayList<String>();
        for (String s : pkgNamesArr) {
            if (s != null && !s.equals("")) {
                pkgNames.add(s);
            }
        }
        return pkgNames;
    }

    public void openPushDialog() {
        if (pushDialog==null) {
            pushDialog = new PushServerInfoDialog();
        }
        PushFacetsDialogWrapper w = new PushFacetsDialogWrapper(project);
        w.pack();
        w.show();
    }

    public boolean push(String url, String username, String password) {
        StatusBar statusBar = WindowManager.getInstance()
                        .getStatusBar(project);
        statusBar.setInfo("Pushing facets to " + url + "...");

        try {

            // retrieve the facet sources from modified facets
            List<String> facetSources = new ArrayList<String>();
            for (WideaFacetDescriptor fd : facetDescriptors) {
                String fqcn = fd.getFacetClassName();
                PsiClass psiClass = getPsiClass(fqcn);
                if (psiClass!=null && psiClass.getLanguage().getID().equals("Groovy")) {
                    facetSources.add(psiClass.getContainingFile().getText());
                }
            }

            // push all this !
            PomHelper pomHelper = AppUtils.getPomHelper(new File(project.getBaseDir().getPath()));
            StringWriter sw = new StringWriter();
            Logger logger = new Logger(sw);
            AppUtils.pushFacetSources(pomHelper, logger, url, username, password, facetSources);

            JBPopupFactory.getInstance()
                    .createHtmlTextBalloonBuilder("Facets pushed !", MessageType.INFO, null)
                    .setFadeoutTime(7500)
                    .createBalloon()
                    .show(RelativePoint.getNorthEastOf(statusBar.getComponent()),
                                                     Balloon.Position.atRight);

            statusBar.setInfo("Facets pushed to " + url);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            String message = "Could not push : " + e.getMessage();
            statusBar.setInfo(message);
            JBPopupFactory.getInstance()
                    .createHtmlTextBalloonBuilder(message, MessageType.ERROR, null)
                    .setFadeoutTime(7500)
                    .createBalloon()
                    .show(RelativePoint.getNorthEastOf( statusBar.getComponent()),
                                                     Balloon.Position.atRight);
            return false;
        }
    }

}


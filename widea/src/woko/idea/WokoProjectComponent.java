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
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import net.sourceforge.jfacets.FacetDescriptor;
import org.jetbrains.annotations.NotNull;
import woko.tooling.cli.Runner;
import woko.tooling.utils.Logger;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.io.File;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

public class WokoProjectComponent implements ProjectComponent {

    private final Project project;
    private JavaPsiFacade psiFacade;
    private GlobalSearchScope projectScope;

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
        PsiClass c = psiFacade.findClass(fqcn, projectScope);
        if (c!=null) {
            c.getContainingFile().navigate(true);
            return true;
        }
        return false;
    }

    private FacetDescriptorTableModel createFacetsTableModel(Map<String,Long> pushStamps) {
        // invoke runner to grab the facets in the project
        VirtualFile baseDir = project.getBaseDir();
        if (baseDir==null) {
            return null;
        }
        File projectRootDir = new File(baseDir.getPath());
        StringWriter sw = new StringWriter();
        Logger logger = new Logger(sw);
        Runner runner = new Runner(logger, projectRootDir);
        try {
            Object result = runner.invokeCommand("list", "facets", "customClassLoader");
            if (result instanceof List) {
                @SuppressWarnings("unchecked")
                List<FacetDescriptor> descriptors = (List<FacetDescriptor>)result;
                return new FacetDescriptorTableModel(project, descriptors, pushStamps);
            }
            return null;
        } catch(Exception e) {
            return null;
        }
    }

    public FacetDescriptorTableModel initializeFacetsTable(JTable table) {
        // retrieve push states for the old model
        FacetDescriptorTableModel oldModel = getFacetTableModel(table);
        Map<String,Long> oldPushStamps = null;
        if (oldModel!=null) {
            oldPushStamps = oldModel.getPushStamps();
        }

        FacetDescriptorTableModel model = createFacetsTableModel(oldPushStamps);
        if (model!=null) {
            TableRowSorter<FacetDescriptorTableModel> sorter = new TableRowSorter<FacetDescriptorTableModel>(model);
            table.setModel(model);
            table.setRowSorter(sorter);
            TableCellRenderer renderer = new FacetTableCellRenderer();
            TableColumnModel colModel = table.getColumnModel();
            colModel.getColumn(0).setWidth(70);
            for (int i=0;i<model.getColumnCount();i++) {
                colModel.getColumn(i).setCellRenderer(renderer);
            }
        }
        return model;
    }

    public FacetDescriptorTableModel getFacetTableModel(JTable table) {
        TableModel m = table.getModel();
        if (m instanceof FacetDescriptorTableModel) {
            return (FacetDescriptorTableModel)m;
        }
        return null;
    }

    public static abstract class FilterCallback {

        protected abstract boolean matches(FacetDescriptor fd);

        protected boolean strMatch(String s, String filterText) {
            return filterText == null
                    || filterText.equals("")
                    || s == null
                    || s.equals("")
                    || s.toLowerCase().contains(filterText.toLowerCase());
        }

        protected boolean fdMatch(FacetDescriptor fd, String filterText) {
            return strMatch(fd.getName(), filterText)
                    || strMatch(fd.getProfileId(), filterText)
                    || strMatch(fd.getTargetObjectType().getName(), filterText)
                    || strMatch(fd.getFacetClass().getName(), filterText);
        }

    }

    public void setFacetTableFilterCallback(JTable table, final FilterCallback callback) {
        TableRowSorter<FacetDescriptorTableModel> sorter = (TableRowSorter<FacetDescriptorTableModel>)table.getRowSorter();
        if (sorter!=null) {
            sorter.setRowFilter(new RowFilter<FacetDescriptorTableModel,Integer>() {
                @Override
                public boolean include(Entry<? extends FacetDescriptorTableModel, ? extends Integer> entry) {
                    FacetDescriptorTableModel model = entry.getModel();
                    FacetDescriptor fd = model.getFacetDescriptorAt(entry.getIdentifier());
                    return callback.matches(fd);
                }
            });
        }
    }

}


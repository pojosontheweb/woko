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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import net.sourceforge.jfacets.FacetDescriptor;
import woko.tooling.cli.Runner;
import woko.tooling.utils.Logger;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class WokoToolWindow implements ToolWindowFactory {
    private JPanel panel1;
    private JTable table1;
    private JButton reloadButton;
    private JTextField textFieldFilter;
    private JButton clearButton;

    private Project project;
    private TableRowSorter<FacetDescriptorTableModel> sorter;

    public WokoToolWindow() {
        reloadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                refresh();
            }
        });
        textFieldFilter.getDocument().addDocumentListener(
            new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    filter();
                }
                public void insertUpdate(DocumentEvent e) {
                    filter();
                }
                public void removeUpdate(DocumentEvent e) {
                    filter();
                }
            });
        table1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount()==2) {
                    // dbl-clicked : get selected row
                    openFacets(table1.convertRowIndexToModel(table1.getSelectedRow()));
                }
            }
        });
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                textFieldFilter.setText(null);
            }
        });
        table1.setIntercellSpacing(new Dimension(0,0));
    }

    private void openFacets(int... rows) {
        JavaPsiFacade psiFacade = JavaPsiFacade.getInstance(project);
        GlobalSearchScope projectScope = GlobalSearchScope.projectScope(project);
        FacetDescriptorTableModel model = (FacetDescriptorTableModel)table1.getModel();
        for (int i : rows) {
            String projectFileName = model.getProjectFile(i);
            if (projectFileName!=null) {
                PsiClass c = psiFacade.findClass(model.getProjectFile(i), projectScope);
                if (c!=null) {
                    c.getContainingFile().navigate(true);
                }
            }
        }
    }

    private void refresh() {
        // invoke runner to grab the facets in the project
        VirtualFile baseDir = project.getBaseDir();
        File projectRootDir = new File(baseDir.getPath());
        StringWriter sw = new StringWriter();
        Logger logger = new Logger(sw);
        Runner runner = new Runner(logger, projectRootDir);
        Object result = runner.invokeCommand("list", "facets", "customClassLoader");
        if (result instanceof List) {
            List<FacetDescriptor> descriptors = (List<FacetDescriptor>)result;
            FacetDescriptorTableModel tableModel = new FacetDescriptorTableModel(project, descriptors);
            // enable sort / filter of the jtable
            sorter = new TableRowSorter<FacetDescriptorTableModel>(tableModel);
            table1.setModel(tableModel);
            table1.setRowSorter(sorter);
            TableCellRenderer renderer = new FacetTableCellRenderer();
            TableColumnModel colModel = table1.getColumnModel();
            colModel.getColumn(0).setWidth(70);
            for (int i=0;i<tableModel.getColumnCount();i++) {
                colModel.getColumn(i).setCellRenderer(renderer);
            }
        }
        textFieldFilter.setEnabled(true);
    }

    private void filter() {
        sorter.setRowFilter(new RowFilter<FacetDescriptorTableModel,Integer>() {
            @Override
            public boolean include(Entry<? extends FacetDescriptorTableModel, ? extends Integer> entry) {
                FacetDescriptorTableModel model = entry.getModel();
                FacetDescriptor fd = model.getFacetDescriptorAt(entry.getIdentifier());
                String filterText = textFieldFilter.getText();
                return (strMatch(fd.getName(), filterText)
                    || strMatch(fd.getProfileId(), filterText)
                    || strMatch(fd.getTargetObjectType().getName(), filterText)
                    || strMatch(fd.getFacetClass().getName(), filterText));
            }

            private boolean strMatch(String s, String filterText) {
                return filterText == null
                        || filterText.equals("")
                        || s == null
                        || s.equals("")
                        || s.toLowerCase().contains(filterText.toLowerCase());
            }
        });
    }

    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        this.project = project;
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(panel1, "", false);
        toolWindow.getContentManager().addContent(content);
    }
}

class FacetDescriptorTableModel extends AbstractTableModel {

    private final List<FacetDescriptor> facetDescriptors;
    private final String[] facetLocalFiles;

    private static final String[] COLUMNS = new String[] { "name", "profileId", "targetObjectType", "facetClass" };

    FacetDescriptorTableModel(Project project, List<FacetDescriptor> descriptors) {
        this.facetDescriptors = descriptors;
        this.facetLocalFiles = new String[descriptors.size()];
        // iterate on descriptors and set the local files if
        // any for later use
        JavaPsiFacade psiFacade = JavaPsiFacade.getInstance(project);
        GlobalSearchScope globalSearchScope = GlobalSearchScope.projectScope(project);
        for (int i=0 ; i<descriptors.size(); i++) {
            // check if the file is defined in the project
            String facetClassName = descriptors.get(i).getFacetClass().getName();
            PsiClass psiClass = psiFacade.findClass(facetClassName, globalSearchScope);
            if (psiClass!=null) {
                facetLocalFiles[i] = psiClass.getQualifiedName();
            }
        }
    }

    @Override
    public String getColumnName(int i) {
        return COLUMNS[i];
    }

    public int getRowCount() {
        return facetDescriptors.size();
    }

    public int getColumnCount() {
        return 4;
    }

    public Object getValueAt(int row, int col) {
        FacetDescriptor fd = facetDescriptors.get(row);
        switch (col) {
            case 0 : return fd.getName();
            case 1 : return fd.getProfileId();
            case 2 : return fd.getTargetObjectType().getName();
            case 3 : return fd.getFacetClass().getName();
            default: throw new ArrayIndexOutOfBoundsException("col is out of bounds : " + col);
        }
    }

    public FacetDescriptor getFacetDescriptorAt(int i) {
        return facetDescriptors.get(i);
    }

    public String getProjectFile(int row) {
        return facetLocalFiles[row];
    }

}

class FacetTableCellRenderer extends DefaultTableCellRenderer {

    public Component getTableCellRendererComponent(
                            JTable table, Object value,
                            boolean isSelected, boolean hasFocus,
                            int row, int column) {
        FacetDescriptorTableModel model = (FacetDescriptorTableModel)table.getModel();
        // is the class a project class ?
        if (model.getProjectFile(row)==null) {
            setBackground(new Color(230,230,230));
        } else {
            setBackground(Color.white);
        }
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.lightGray));
        return this;
    }
}

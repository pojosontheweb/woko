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
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import net.sourceforge.jfacets.FacetDescriptor;
import woko.tooling.cli.Runner;
import woko.tooling.utils.Logger;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.StringWriter;
import java.util.List;

public class WokoToolWindow implements ToolWindowFactory {
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JTable table1;
    private JButton reloadButton;
    private JButton newButton;
    private JButton pushSelectedButton;
    private JTextField textFieldFilter;

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
            FacetDescriptorTableModel tableModel = new FacetDescriptorTableModel(descriptors);
            // enable sort / filter of the jtable
            sorter = new TableRowSorter<FacetDescriptorTableModel>(tableModel);
            table1.setModel(tableModel);
            table1.setRowSorter(sorter);
        }
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

    private static final String[] COLUMNS = new String[] { "select", "name", "profileId", "targetObjectType", "facetClass" };

    FacetDescriptorTableModel(List<FacetDescriptor> descriptors) {
        this.facetDescriptors = descriptors;
    }

    @Override
    public String getColumnName(int i) {
        return COLUMNS[i];
    }

    public int getRowCount() {
        return facetDescriptors.size();
    }

    public int getColumnCount() {
        return 5;
    }

    public Object getValueAt(int row, int col) {
        FacetDescriptor fd = facetDescriptors.get(row);
        switch (col) {
            case 0 : return true;
            case 1 : return fd.getName();
            case 2 : return fd.getProfileId();
            case 3 : return fd.getTargetObjectType().getName();
            case 4 : return fd.getFacetClass().getName();
            default: throw new ArrayIndexOutOfBoundsException("col is out of bounds : " + col);
        }
    }

    public FacetDescriptor getFacetDescriptorAt(int i) {
        return facetDescriptors.get(i);
    }

}

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
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
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
    private FacetDescriptorTableModel tableModel;

    public WokoToolWindow() {
        reloadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                refresh();
            }
        });
        textFieldFilter.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent keyEvent) {
                if (tableModel!=null) {
                    tableModel.filter(textFieldFilter.getText());
                }
            }

            public void keyPressed(KeyEvent keyEvent) {
            }

            public void keyReleased(KeyEvent keyEvent) {
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
            tableModel = new FacetDescriptorTableModel(descriptors);
            table1.setModel(tableModel);
        }
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
    private List<FacetDescriptor> filtered;

    private static final String[] COLUMNS = new String[] { "select", "name", "profileId", "targetObjectType", "facetClass" };

    FacetDescriptorTableModel(List<FacetDescriptor> descriptors) {
        this.facetDescriptors = descriptors;
        this.filtered = descriptors;
    }

    @Override
    public String getColumnName(int i) {
        return COLUMNS[i];
    }

    private boolean strMatch(String s, String filterText) {
        return filterText == null
                || filterText.equals("")
                || s == null
                || s.equals("")
                || s.toLowerCase().contains(filterText.toLowerCase());
    }

    private List<FacetDescriptor> filterList(String filterText) {
        List<FacetDescriptor> filtered = new ArrayList<FacetDescriptor>();
        for(FacetDescriptor fd : facetDescriptors) {
            if (strMatch(fd.getName(), filterText)
                    || strMatch(fd.getProfileId(), filterText)
                    || strMatch(fd.getTargetObjectType().getName(), filterText)
                    || strMatch(fd.getFacetClass().getName(), filterText)) {
                filtered.add(fd);
            }
        }
        return filtered;
    }

    public int getRowCount() {
        return filtered.size();
    }

    public int getColumnCount() {
        return 5;
    }

    public Object getValueAt(int row, int col) {
        FacetDescriptor fd = filtered.get(row);
        switch (col) {
            case 0 : return true;
            case 1 : return fd.getName();
            case 2 : return fd.getProfileId();
            case 3 : return fd.getTargetObjectType().getName();
            case 4 : return fd.getFacetClass().getName();
            default: throw new ArrayIndexOutOfBoundsException("col is out of bounds : " + col);
        }
    }

    public void filter(String text) {
        this.filtered = filterList(text);
        fireTableDataChanged();
    }
}

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
import java.io.File;
import java.io.StringWriter;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: vankeisb
 * Date: 16/05/12
 * Time: 10:54
 * To change this template use File | Settings | File Templates.
 */
public class FacetsTable implements ToolWindowFactory {
    private JTextField textFieldFilter;
    private JButton refreshButton;
    private JButton pushButton;
    private JPanel mainPanel;
    private JTable facetsTable;
    private ToolWindow toolWindow;

    public FacetsTable() {
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                doRefresh();
            }
        });
    }

    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        this.toolWindow = toolWindow;
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(mainPanel, "", false);
        toolWindow.getContentManager().addContent(content);

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
            facetsTable.setModel(tableModel);
        }
    }

    public void doRefresh() {
    }

}

class FacetDescriptorTableModel extends AbstractTableModel {

    private final List<FacetDescriptor> facetDescriptors;

    private static final String[] COLUMNS = new String[] { "name", "profileId", "targetObjectType", "facetClass" };

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
}
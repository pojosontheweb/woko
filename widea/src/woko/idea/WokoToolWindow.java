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
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import net.sourceforge.jfacets.FacetDescriptor;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class WokoToolWindow implements ToolWindowFactory {

    private JPanel panel1;
    private JTable table1;
    private JButton reloadButton;
    private JTextField textFieldFilter;
    private JButton clearButton;
    private JCheckBox includeLibsCheckBox;

    private Project project;

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
                    int row = table1.convertRowIndexToModel(table1.getSelectedRow());
                    FacetDescriptorTableModel model = (FacetDescriptorTableModel)table1.getModel();
                    FacetDescriptor fd = model.getFacetDescriptorAt(row);
                    getWpc().openClassInEditor(model.getProjectFile(fd));
                }
            }
        });
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                textFieldFilter.setText(null);
            }
        });
        table1.setIntercellSpacing(new Dimension(0,0));
        includeLibsCheckBox.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent) {
                filter();
            }
        });
    }

    private WokoProjectComponent getWpc() {
        return project.getComponent(WokoProjectComponent.class);
    }

    private void refresh() {
        FacetDescriptorTableModel fdm = getWpc().initializeFacetsTable(table1);
        if (fdm!=null) {
            textFieldFilter.setEnabled(true);
        }
    }

    private void filter() {
        final WokoProjectComponent wpc = getWpc();
        wpc.setFacetTableFilterCallback(table1, new WokoProjectComponent.FilterCallback() {
            @Override
            protected boolean matches(FacetDescriptor fd) {
                String text = textFieldFilter.getText();
                boolean fdMatch = fdMatch(fd, text);
                if (fdMatch) {
                    // do we include libs or not ?
                    if (!includeLibsCheckBox.isSelected()) {
                        // check if class is project class
                        return wpc.getFacetTableModel(table1).getProjectFile(fd) != null;
                    }
                }
                return fdMatch;
            }
        });
    }

    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        this.project = project;
        table1.setModel(new FacetDescriptorTableModel(project, new ArrayList<FacetDescriptor>(), null));
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(panel1, "", false);
        toolWindow.getContentManager().addContent(content);
        refresh();
    }

}


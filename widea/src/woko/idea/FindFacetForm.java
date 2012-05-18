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
import net.sourceforge.jfacets.FacetDescriptor;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by IntelliJ IDEA.
 * User: vankeisb
 * Date: 18/05/12
 * Time: 13:14
 * To change this template use File | Settings | File Templates.
 */
public class FindFacetForm {
    private JTextField textFieldFilter;
    private JTable table1;
    private JPanel panel1;

    private Project project;

    public FindFacetForm(Project project) {
        this.project = project;
        table1.setIntercellSpacing(new Dimension(0,0));
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

        final WokoProjectComponent wpc = getWpc();
        textFieldFilter.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                int curSelection = table1.getSelectedRow();
                if (keyEvent.getKeyCode()==KeyEvent.VK_ENTER) {
                    if (curSelection!=-1) {
                        FacetDescriptor fd = wpc
                                .getFacetTableModel(table1)
                                .getFacetDescriptorAt(
                                        table1.getRowSorter()
                                                .convertRowIndexToModel(curSelection)
                                );
                        if (fd!=null) {
                            onEnter(fd);
                        }
                    }
                }
                int move = 0;
                if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
                    move = -1;
                } else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                    move = 1;
                }
                if (move != 0) {
                    int viewRowCount = table1.getRowSorter().getViewRowCount();
                    if (curSelection == -1) {
                        // set first row as selected if no row selected yet
                        if (viewRowCount > 0) {
                            table1.getSelectionModel().setSelectionInterval(0, 0);
                        }
                    } else {
                        // something selected : apply move
                        curSelection = curSelection + move;
                        // manage out of bounds
                        if (curSelection < 0) {
                            curSelection = viewRowCount - 1;
                        } else if (curSelection >= viewRowCount) {
                            curSelection = 0;
                        }
                        table1.getSelectionModel().setSelectionInterval(curSelection, curSelection);
                    }
                }
            }
        });

        refresh();
        filter();
    }

    private WokoProjectComponent getWpc() {
        return project.getComponent(WokoProjectComponent.class);
    }

    private void refresh() {
        if (project!=null) {
            getWpc().initializeFacetsTable(table1);
        }
    }

    private void filter() {
        final WokoProjectComponent wpc = getWpc();
        wpc.setFacetTableFilterCallback(table1, new WokoProjectComponent.FilterCallback() {
            @Override
            protected boolean matches(FacetDescriptor fd) {
                return fdMatch(fd, textFieldFilter.getText())
                        && wpc.getFacetTableModel(table1).getProjectFile(fd) != null;
            }
        });
        RowSorter<?> rowSorter = table1.getRowSorter();
        if (rowSorter!=null) {
            int viewRowCount = rowSorter.getViewRowCount();
            if (viewRowCount>=1) {
                table1.getSelectionModel().setSelectionInterval(0, 0);
            }
        }
    }

    public JComponent getComponent() {
        return panel1;
    }

    protected void onEnter(FacetDescriptor fd) {

    }
}

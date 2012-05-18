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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class FindFacetDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable table1;
    private JTextField textFieldFilter;

    private Project project;

    public FindFacetDialog(Project project) {
        setTitle("Find Facet...");
        this.project = project;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                cancel();
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

        final WokoProjectComponent wpc = getWpc();
        textFieldFilter.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                int move = 0;
                if (keyEvent.getKeyCode()==KeyEvent.VK_UP) {
                    move = -1;
                } else if (keyEvent.getKeyCode()==KeyEvent.VK_DOWN) {
                    move = 1;
                }
                if (move!=0) {
                    int curSelection = table1.getSelectedRow();
                    int viewRowCount = table1.getRowSorter().getViewRowCount();
                    if (curSelection==-1) {
                        // set first row as selected if no row selected yet
                        if (viewRowCount>0) {
                            table1.getSelectionModel().setSelectionInterval(0, 0);
                        }
                    } else {
                        // something selected : apply move
                        curSelection = curSelection + move;
                        // manage out of bounds
                        if (curSelection<0) {
                            curSelection = viewRowCount-1;
                        } else if (curSelection>=viewRowCount) {
                            curSelection = 0;
                        }
                        table1.getSelectionModel().setSelectionInterval(curSelection, curSelection);
                    }
                }
            }
        });

        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                cancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

    }

    private void cancel() {
        this.setVisible(false);
    }

    private WokoProjectComponent getWpc() {
        return project.getComponent(WokoProjectComponent.class);
    }

    public FindFacetDialog refresh() {
        if (project!=null) {
            getWpc().initializeFacetsTable(table1);
        }
        return this;
    }

    public FindFacetDialog filter() {
        final WokoProjectComponent wpc = getWpc();
        wpc.setFacetTableFilterCallback(table1, new WokoProjectComponent.FilterCallback() {
            @Override
            protected boolean matches(FacetDescriptor fd) {
                return fdMatch(fd, textFieldFilter.getText())
                        && wpc.getFacetTableModel(table1).getProjectFile(fd) != null;
            }
        });
        int viewRowCount = table1.getRowSorter().getViewRowCount();
        if (viewRowCount>=1) {
            table1.getSelectionModel().setSelectionInterval(0, 0);
        }
        buttonOK.setEnabled(viewRowCount>1);
        return this;
    }

    public static void main(String[] args) {
        FindFacetDialog dialog = new FindFacetDialog(null);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}

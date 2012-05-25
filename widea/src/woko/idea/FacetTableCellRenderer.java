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
import com.intellij.psi.PsiClass;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

class FacetTableCellRenderer extends DefaultTableCellRenderer {

    private final Project project;

    private static final ImageIcon ICON_COMPILED =
            new ImageIcon(FacetTableCellRenderer.class.getResource("/woko/idea/javaClass.png"));

    private static final ImageIcon ICON_JAVA =
            new ImageIcon(FacetTableCellRenderer.class.getResource("/woko/idea/java.png"));

    private static final ImageIcon ICON_GROOVY =
            new ImageIcon(FacetTableCellRenderer.class.getResource("/woko/idea/groovy.png"));

    FacetTableCellRenderer(Project project) {
        this.project = project;
    }

    private WokoProjectComponent wpc() {
        return project.getComponent(WokoProjectComponent.class);
    }

    public Component getTableCellRendererComponent(
                            JTable table, Object value,
                            boolean isSelected, boolean hasFocus,
                            int row, int column) {
        FacetDescriptorTableModel model = (FacetDescriptorTableModel)table.getModel();
        WideaFacetDescriptor fd = model.getFacetDescriptorAt(table.getRowSorter().convertRowIndexToModel(row));
        if (fd!=null) {
            // is the class a project class ?
            PsiClass psiClass = wpc().getPsiClass(fd.getFacetClassName());
            if (psiClass==null) {
                setBackground(new Color(230,230,230));
            } else {
                setBackground(Color.white);
            }
        }

        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.lightGray));

        // check if the file has changed since last refresh
        if (wpc().isModifiedSinceLastRefresh(fd)) {
            setFont(getFont().deriveFont(Font.BOLD));
        }

        // add an icon to first column's label to show the language
        if (column==0) {
            FdType t = fd.getType();
            switch(t) {
                case Compiled   : setIcon(ICON_COMPILED); break;
                case Groovy     : setIcon(ICON_GROOVY); break;
                case Java       : setIcon(ICON_JAVA); break;
            }
        } else {
            setIcon(null);
        }
        return this;
    }
}

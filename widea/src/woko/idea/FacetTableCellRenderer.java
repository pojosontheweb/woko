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
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import net.sourceforge.jfacets.FacetDescriptor;
import sun.font.Font2D;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: vankeisb
 * Date: 18/05/12
 * Time: 10:32
 * To change this template use File | Settings | File Templates.
 */
class FacetTableCellRenderer extends DefaultTableCellRenderer {

    private final Project project;

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
        WokoProjectComponent w = wpc();
        // is the class a project class ?
        FacetDescriptor fd = model.getFacetDescriptorAt(table.convertRowIndexToModel(row));
        PsiClass psiClass = w.getPsiClass(fd.getFacetClass().getName());
        if (psiClass==null) {
            setBackground(new Color(230,230,230));
        } else {
            setBackground(Color.white);
        }

        // check if the file has changed since last refresh
        Long lastRefreshStamp = w.getLastRefreshStamp(fd);
        boolean needsPush = false;
        if (lastRefreshStamp!=null) {
            // check if the file has been modified since last push
            if (psiClass!=null) {
                PsiFile f = psiClass.getContainingFile();
                if (f!=null) {
                    long modifStamp = f.getModificationStamp();
                    needsPush = modifStamp!=lastRefreshStamp;
                }
            }
        }
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.lightGray));
        if (needsPush) {
            setFont(getFont().deriveFont(Font.BOLD));
        }
        return this;
    }
}

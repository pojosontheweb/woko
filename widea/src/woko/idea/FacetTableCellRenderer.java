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

import net.sourceforge.jfacets.FacetDescriptor;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: vankeisb
 * Date: 18/05/12
 * Time: 10:32
 * To change this template use File | Settings | File Templates.
 */
class FacetTableCellRenderer extends DefaultTableCellRenderer {

    public Component getTableCellRendererComponent(
                            JTable table, Object value,
                            boolean isSelected, boolean hasFocus,
                            int row, int column) {
        FacetDescriptorTableModel model = (FacetDescriptorTableModel)table.getModel();
        // is the class a project class ?
        FacetDescriptor fd = model.getFacetDescriptorAt(table.convertRowIndexToModel(row));
        if (model.getProjectFile(fd)==null) {
            setBackground(new Color(230,230,230));
        } else {
            setBackground(Color.white);
        }
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.lightGray));
        return this;
    }
}

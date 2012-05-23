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

import javax.swing.table.AbstractTableModel;
import java.util.List;

class FacetDescriptorTableModel extends AbstractTableModel {

    private final Project project;

    private static final String[] COLUMNS = new String[] { "name", "profileId", "targetObjectType", "facetClass" };

    FacetDescriptorTableModel(Project project) {
        this.project = project;
    }

    private WokoProjectComponent wpc() {
        return project.getComponent(WokoProjectComponent.class);
    }

    @Override
    public String getColumnName(int i) {
        return COLUMNS[i];
    }

    private List<WideaFacetDescriptor> getFds() {
        return wpc().getFacetDescriptors();
    }

    public int getRowCount() {
        return getFds().size();
    }

    public int getColumnCount() {
        return 4;
    }

    public Object getValueAt(int row, int col) {
        WideaFacetDescriptor fd = getFacetDescriptorAt(row);
        if (fd==null) {
            return null;
        }
        switch (col) {
            case 0 : return fd.getName();
            case 1 : return fd.getProfileId();
            case 2 : return fd.getTargetObjectTypeName();
            case 3 : return fd.getFacetClassName();
            default: throw new ArrayIndexOutOfBoundsException("col is out of bounds : " + col);
        }
    }

    public WideaFacetDescriptor getFacetDescriptorAt(int row) {
        List<WideaFacetDescriptor> facetDescriptors = getFds();
        if (row>=facetDescriptors.size()) {
            return null;
        }
        return facetDescriptors.get(row);
    }
}

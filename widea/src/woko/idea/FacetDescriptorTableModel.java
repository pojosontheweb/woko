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
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import net.sourceforge.jfacets.FacetDescriptor;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: vankeisb
 * Date: 18/05/12
 * Time: 10:32
 * To change this template use File | Settings | File Templates.
 */
class FacetDescriptorTableModel extends AbstractTableModel {

    private final List<FacetDescriptor> facetDescriptors;
    private final String[] facetLocalFiles;

    private static final String[] COLUMNS = new String[] { "name", "profileId", "targetObjectType", "facetClass" };

    FacetDescriptorTableModel(Project project, List<FacetDescriptor> descriptors) {
        this.facetDescriptors = descriptors;
        this.facetLocalFiles = new String[descriptors.size()];
        // iterate on descriptors and set the local files if
        // any for later use
        JavaPsiFacade psiFacade = JavaPsiFacade.getInstance(project);
        GlobalSearchScope globalSearchScope = GlobalSearchScope.projectScope(project);
        for (int i=0 ; i<descriptors.size(); i++) {
            // check if the file is defined in the project
            String facetClassName = descriptors.get(i).getFacetClass().getName();
            PsiClass psiClass = psiFacade.findClass(facetClassName, globalSearchScope);
            if (psiClass!=null) {
                facetLocalFiles[i] = psiClass.getQualifiedName();
            }
        }
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

    public FacetDescriptor getFacetDescriptorAt(int i) {
        return facetDescriptors.get(i);
    }

    public String getProjectFile(FacetDescriptor fd) {
        int i = facetDescriptors.indexOf(fd);
        if (i==-1) {
            return null;
        }
        return facetLocalFiles[i];
    }

}

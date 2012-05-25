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

import javax.swing.*;

public class FacetTypeCellRenderer extends FacetCellRenderer {

    public FacetTypeCellRenderer(Project project) {
        super(project);
    }

    private static final ImageIcon ICON_COMPILED =
            new ImageIcon(FacetTypeCellRenderer.class.getResource("/woko/idea/javaClass.png"));

    private static final ImageIcon ICON_JAVA =
            new ImageIcon(FacetTypeCellRenderer.class.getResource("/woko/idea/java.png"));

    private static final ImageIcon ICON_GROOVY =
            new ImageIcon(FacetTypeCellRenderer.class.getResource("/woko/idea/groovy.png"));

    @Override
    protected void setDisplayValue(JTable table, Object value, boolean selected, boolean hasFocus, int row, int column) {
        setValue(null);
        FdType type = (FdType)value;
        switch(type) {
            case Compiled   : setIcon(ICON_COMPILED); break;
            case Groovy     : setIcon(ICON_GROOVY); break;
            case Java       : setIcon(ICON_JAVA); break;
            default         : setIcon(null); break;
        }
    }
}

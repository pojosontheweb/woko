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
import com.intellij.openapi.ui.DialogWrapper;
import net.sourceforge.jfacets.FacetDescriptor;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: vankeisb
 * Date: 18/05/12
 * Time: 14:01
 * To change this template use File | Settings | File Templates.
 */
class FindFacetDialogWrapper extends DialogWrapper {

    private Project project;

    FindFacetDialogWrapper(Project project) {
        super(project);
        this.project = project;
        init();
    }

    @Override
    protected JComponent createCenterPanel() {
        return new FindFacetForm(project) {
            @Override
            protected void onEnter(FacetDescriptor fd) {
                WokoProjectComponent wpc = project.getComponent(WokoProjectComponent.class);
                wpc.openClassInEditor(fd.getFacetClass().getName());
                dispose();
            }
        }
        .getComponent();
    }

    @Override
    protected Action[] createActions() {
        return new Action[0];
    }
}

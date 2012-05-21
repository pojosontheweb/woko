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

class PushFacetsDialogWrapper extends DialogWrapper {

    private Project project;
    private PushServerInfoDialog centerPanel;

    PushFacetsDialogWrapper(Project project) {
        super(project);
        this.project = project;
        init();
        this.setResizable(false);
        centerPanel.setAppUrl("http://localhost:8080/" + project.getName());
    }

    @Override
    protected JComponent createCenterPanel() {
        centerPanel = new PushServerInfoDialog() {
            @Override
            protected void onPush(String url, String username, String password) {
                // invoke the push routines
                WokoProjectComponent wpc = project.getComponent(WokoProjectComponent.class);
                wpc.push(url, username, password);
            }
        };
        return centerPanel.getComponent();
    }

    @Override
    protected Action[] createActions() {
        return new Action[0];
    }
}

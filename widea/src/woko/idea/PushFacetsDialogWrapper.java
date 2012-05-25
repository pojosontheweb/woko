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
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;

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
        centerPanel = new PushServerInfoDialog();
        return centerPanel.getComponent();
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
        // invoke the push routines
        WokoProjectComponent wpc = project.getComponent(WokoProjectComponent.class);
        wpc.push(centerPanel.getAppUrl(), centerPanel.getUsername(), centerPanel.getPassword());
    }
}

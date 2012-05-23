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

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: vankeisb
 * Date: 21/05/12
 * Time: 14:54
 * To change this template use File | Settings | File Templates.
 */
public class PushServerInfoDialog {
    private JPanel mainPanel;
    private JTextField textFieldUrl;
    private JTextField textFieldUsername;
    private JPasswordField passwordField;
    private JCheckBox donTAskAgainCheckBox;

    public JComponent getComponent() {
        return mainPanel;
    }

    public void setAppUrl(String s) {
        textFieldUrl.setText(s);
    }

    public String getAppUrl() {
        return textFieldUrl.getText();
    }

    public String getUsername() {
        return textFieldUsername.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }
}

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

import com.intellij.facet.FacetConfiguration;
import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.facet.ui.FacetValidatorsManager;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;

/**
 * Created by IntelliJ IDEA.
 * User: vankeisb
 * Date: 25/05/12
 * Time: 13:54
 * To change this template use File | Settings | File Templates.
 */
public class WokoFacetConfiguration implements FacetConfiguration {

    @Override
    public FacetEditorTab[] createEditorTabs(FacetEditorContext editorContext, FacetValidatorsManager validatorsManager) {
        return new FacetEditorTab[] { new WokoFacetConfigurationTab() };
    }

    @Override
    public void readExternal(Element element) throws InvalidDataException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void writeExternal(Element element) throws WriteExternalException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

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

import com.intellij.facet.Facet;
import com.intellij.facet.FacetType;
import com.intellij.facet.FacetTypeId;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class WokoFacet extends Facet<WokoFacetConfiguration> {

    public static final FacetTypeId<WokoFacet> FACET_TYPE_ID = new FacetTypeId<WokoFacet>("wokoFacetTypeId");

    public WokoFacet(@NotNull FacetType facetType, @NotNull Module module, @NotNull String name, @NotNull WokoFacetConfiguration configuration, Facet underlyingFacet) {
        super(facetType, module, name, configuration, underlyingFacet);
    }

    @Override
    public void initFacet() {
        // register the tool window
        Project project = getModule().getProject();
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        ToolWindow toolWindow = toolWindowManager.registerToolWindow("Woko", true, ToolWindowAnchor.BOTTOM);
        WokoProjectComponent wpc = project.getComponent(WokoProjectComponent.class);
        WokoToolWindow wtw = wpc.getToolWindow();
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(wtw.getMainPanel(), "", false);
        toolWindow.getContentManager().addContent(content);
        toolWindow.setIcon(WokoFacetType.WOKO_ICON);
    }

    @Override
    public void disposeFacet() {
        // unregister the tool window
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(getModule().getProject());
        toolWindowManager.unregisterToolWindow("Woko");
    }
}

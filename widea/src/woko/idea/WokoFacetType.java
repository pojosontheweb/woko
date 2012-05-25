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
import com.intellij.javaee.web.facet.WebFacet;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class WokoFacetType extends FacetType<WokoFacet,WokoFacetConfiguration> {

    public static final Icon WOKO_ICON = IconLoader.findIcon("/woko/idea/woko.png");

    public WokoFacetType() {
        super(WokoFacet.FACET_TYPE_ID, "Woko", "Woko", WebFacet.ID);
    }

    @Override
    public WokoFacetConfiguration createDefaultConfiguration() {
        return new WokoFacetConfiguration();
    }

    @Override
    public WokoFacet createFacet(@NotNull Module module, String name, @NotNull WokoFacetConfiguration configuration, @Nullable Facet underlyingFacet) {
        return new WokoFacet(this, module, name, configuration, underlyingFacet);
    }

    @Override
    public boolean isSuitableModuleType(ModuleType moduleType) {
        return moduleType instanceof JavaModuleType;
    }

        @Override
    public Icon getIcon() {
        return WOKO_ICON;
    }

}

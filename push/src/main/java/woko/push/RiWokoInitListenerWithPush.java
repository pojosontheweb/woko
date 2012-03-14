package woko.push;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import woko.ri.RiWokoInitListener;

public class RiWokoInitListenerWithPush extends RiWokoInitListener {

    @Override
    protected IFacetDescriptorManager createFacetDescriptorManager() {
        return new PushFacetDescriptorManager(super.createFacetDescriptorManager()); // Enable push command
    }
}

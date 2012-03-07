#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )

package ${package};

import net.sourceforge.jfacets.IFacetDescriptorManager;
import woko.hbcompass.HibernateCompassInMemWokoInitListener;
import woko.push.PushFacetDescriptorManager;
import woko.ri.RiWokoInitListener;

public class MyInitListener extends RiWokoInitListener {

    @Override
    protected IFacetDescriptorManager createFacetDescriptorManager() {
        return new PushFacetDescriptorManager(super.createFacetDescriptorManager()); // Enable /push !
    }


}

package test

import net.sourceforge.jfacets.IFacetDescriptorManager
import woko.hbcompass.HibernateCompassInMemWokoInitListener
import woko.push.PushFacetDescriptorManager
import woko.ri.RiWokoInitListener

class BuiltinAuthWTInitIListener extends RiWokoInitListener {

    @Override
    protected IFacetDescriptorManager createFacetDescriptorManager() {
        return new PushFacetDescriptorManager(super.createFacetDescriptorManager()) // Enable /push !
    }


}

package test

import woko.hbcompass.HibernateCompassInMemWokoInitListener
import net.sourceforge.jfacets.IFacetDescriptorManager
import woko.push.PushFacetDescriptorManager

class ContainerAuthWTInitIListener extends HibernateCompassInMemWokoInitListener {

    @Override
    protected IFacetDescriptorManager createFacetDescriptorManager() {
        return new PushFacetDescriptorManager(super.createFacetDescriptorManager()) // Enable /push !
    }


}

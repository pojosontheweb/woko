package woko.groovyinit;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import woko.WokoIocInitListener;
import woko.ioc.WokoIocContainer;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

public class GroovyInitListener<OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager> extends WokoIocInitListener<OsType,UmType,UnsType,FdmType> {


    @Override
    protected WokoIocContainer<OsType, UmType, UnsType, FdmType> createIocContainer() {
        // read the groovy startup script
        //GroovySh

        return null;

    }


}

package woko.facets.builtin.developer;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import woko.facets.builtin.TabularResultFacet;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import java.util.List;

public class ListTabularImpl<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends ListImpl<OsType,UmType,UnsType,FdmType> implements TabularResultFacet {

    @Override
    public String getPath() {
        return "/WEB-INF/woko/jsp/developer/listTabular.jsp";
    }

    @Override
    public List<String> getPropertyNames() {
        return null;
    }
}

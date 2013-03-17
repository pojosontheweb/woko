package woko.ext.blobs.facets;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.Woko;
import woko.ext.blobs.BlobObject;
import woko.facets.builtin.all.RenderPropertyValueImpl;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

@FacetKey(name = "renderPropertyValue_inputStream", profileId = Woko.ROLE_ALL, targetObjectType = BlobObject.class)
public class RenderPropertyValueBlobInputStream<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends RenderPropertyValueImpl<OsType,UmType,UnsType,FdmType> {

    @Override
    public String getPath() {
        return "/WEB-INF/woko/ext/blobs/renderPropertyValueBlobObject_inputStream.jsp";
    }
}

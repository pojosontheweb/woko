package woko.ext.blobs.facets;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.ext.blobs.BlobObject;
import woko.facets.builtin.RenderPropertiesEdit;
import woko.facets.builtin.all.RenderPropertiesEditImpl;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import java.util.Arrays;
import java.util.List;

@FacetKey(name = RenderPropertiesEdit.FACET_NAME, profileId = "blobmanager", targetObjectType = BlobObject.class)
public class RenderPropertiesEditBlobObject<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends RenderPropertiesEditImpl<OsType,UmType,UnsType,FdmType> {

    @Override
    public List<String> getReadOnlyPropertyNames() {
        return Arrays.asList("contentLength", "id");
    }
}

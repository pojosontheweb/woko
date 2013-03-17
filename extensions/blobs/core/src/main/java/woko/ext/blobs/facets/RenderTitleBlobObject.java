package woko.ext.blobs.facets;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.Woko;
import woko.ext.blobs.BlobObject;
import woko.facets.builtin.RenderTitle;
import woko.facets.builtin.all.RenderTitleImpl;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

@FacetKey(name = RenderTitle.FACET_NAME, profileId = Woko.ROLE_ALL, targetObjectType = BlobObject.class)
public class RenderTitleBlobObject<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends RenderTitleImpl<OsType,UmType,UnsType,FdmType> {

    @Override
    public String getTitle() {
        BlobObject o = (BlobObject)getFacetContext().getTargetObject();
        if (o==null) {
            return super.getTitle();
        }
        return o.getFileName();
    }
}

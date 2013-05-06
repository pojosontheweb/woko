package woko.ext.blobs.facets;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.IInstanceFacet;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.StrictBinding;
import woko.ext.blobs.BlobObject;
import woko.facets.BaseResolutionFacet;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

@StrictBinding(
        allow = {"facet.attachment"}
)
@FacetKey(name="download", profileId="blobmanager", targetObjectType = BlobObject.class)
public class DownloadBlob<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseResolutionFacet<OsType,UmType,UnsType,FdmType> implements IInstanceFacet {

    private Boolean attachment;

    public static final String FACET_NAME = "download";

    public Boolean getAttachment() {
        return attachment;
    }

    public void setAttachment(Boolean attachment) {
        this.attachment = attachment;
    }

    protected BlobObject getBlob() {
        return (BlobObject)getFacetContext().getTargetObject();
    }

    @Override
    public Resolution getResolution(ActionBeanContext abc) {
        BlobObject b = getBlob();
        StreamingResolution res = new StreamingResolution(b.getContentType(), b.getInputStream())
                .setLength(b.getContentLength());
        if (attachment!=null) {
            res.setFilename(b.getFileName()).setAttachment(attachment);
        }
        return res;
    }

    @Override
    public boolean matchesTargetObject(Object targetObject) {
        return getBlob()!=null;
    }
}

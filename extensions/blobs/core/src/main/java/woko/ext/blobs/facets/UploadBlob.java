package woko.ext.blobs.facets;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import woko.ext.blobs.BlobObject;
import woko.ext.blobs.BlobStore;
import woko.facets.BaseResolutionFacet;
import woko.facets.builtin.View;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;
import woko.util.LinkUtil;

import java.io.IOException;

/**
 * Facet for uploading files and creating {@link BlobObject}s from the upload. Assigned to
 * developer, can be reused for any file upload using Woko's {@link BlobStore}s.
 */
@StrictBinding(
        allow = {"facet.data"}
)
@FacetKey(name = "upload", profileId = "developer", targetObjectType = BlobObject.class)
public class UploadBlob<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseResolutionFacet<OsType,UmType,UnsType,FdmType> {

    @Validate(required = true)
    private FileBean data;

    public FileBean getData() {
        return data;
    }

    public void setData(FileBean data) {
        this.data = data;
    }

    @Override
    public Resolution getResolution(ActionBeanContext abc) {
        BlobStore blobStore = getWoko().getIoc().getComponent(BlobStore.KEY);
        if (blobStore==null) {
            throw new IllegalStateException("Could not find BlobStore in IOC with key " + BlobStore.KEY);
        }
        // retrieve the target object : if passed, this means that we update
        // an existing object. It can be null in case we upload a blob for the first time.
        BlobObject blobToUpdate = (BlobObject)getFacetContext().getTargetObject();
        try {
            BlobObject blobObject = blobStore.save(data.getInputStream(), data.getFileName(), data.getContentType(), data.getSize(), blobToUpdate);
            return afterSave(abc, blobObject);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Return the resolution after the upload has completed. Defaults to /view on the
     * uploaded BlobObject. Can be overriden in order to change behavior after upload has completed.
     * @param abc the ActionBeanContext
     * @return the Resolution to be returned after the upload has completed.
     */
    protected Resolution afterSave(ActionBeanContext abc, BlobObject blobObject) {
        abc.getMessages().add(new LocalizableMessage("woko.ext.blobs.upload.ok"));
        return new RedirectResolution(LinkUtil.getUrl(getWoko(), blobObject, View.FACET_NAME));
    }
}

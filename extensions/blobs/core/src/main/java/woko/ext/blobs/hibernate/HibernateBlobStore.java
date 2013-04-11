package woko.ext.blobs.hibernate;

import woko.ext.blobs.BlobObject;
import woko.ext.blobs.BlobStore;
import woko.hibernate.HibernateStore;

import javax.activation.MimetypesFileTypeMap;
import java.io.InputStream;

public class HibernateBlobStore implements BlobStore {

    private final HibernateStore hbStore;

    public HibernateBlobStore(HibernateStore hbStore) {
        this.hbStore = hbStore;
    }

    @Override
    public BlobObject save(InputStream inputStream, String fileName, String contentType, long length, BlobObject blob) {
        HibernateBlob o;
        if (blob==null) {
            // must create the instance...
            o = new HibernateBlob();
        } else {
            // update the blob object
            o = (HibernateBlob)blob;
        }
        o.setBlobData(hbStore.getSession().getLobHelper().createBlob(inputStream, length));
        o.setFileName(fileName);
        o.setContentType(contentType);
        hbStore.save(o);
        return o;
    }

    @Override
    public BlobObject getBlob(Class<? extends BlobObject> blobClass, Long id) {
        return (BlobObject)hbStore.getSession().get(blobClass, id);
    }

}

package woko.ext.blobs.hibernate;

import woko.ext.blobs.BlobObject;
import woko.ext.blobs.BlobStore;
import woko.hibernate.HibernateStore;

import javax.activation.MimetypesFileTypeMap;
import java.io.InputStream;

public class HibernateBlobStore implements BlobStore {

    private final HibernateStore hbStore;

    protected static final MimetypesFileTypeMap MIME_TYPES = new MimetypesFileTypeMap();

    public HibernateBlobStore(HibernateStore hbStore) {
        this.hbStore = hbStore;
    }

    @Override
    public BlobObject save(InputStream inputStream, String fileName, long length, BlobObject blob) {
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
        o.setContentType(computeContentType(fileName));
        hbStore.save(o);
        return o;
    }

    @Override
    public BlobObject getBlob(Long id) {
        return (HibernateBlob)hbStore.getSession().get(HibernateBlob.class, id);
    }

    protected String computeContentType(String fileName) {
        String mimeType = MIME_TYPES.getContentType(fileName);
        if (mimeType==null) {
            // create mime type using file extension only
            int indexOfDot = fileName.indexOf('.');
            if (indexOfDot!=-1) {
                fileName = fileName.substring(indexOfDot);
            }
            mimeType = "binary/" + fileName;
        }
        return mimeType;
    }
}

package woko.ext.blobs.hibernate;

import woko.ext.blobs.BlobObject;
import woko.ext.blobs.BlobStore;
import woko.hibernate.HibernateStore;

import java.io.InputStream;

public class HibernateBlobStore implements BlobStore {

    private final HibernateStore hbStore;

    public HibernateBlobStore(HibernateStore hbStore) {
        this.hbStore = hbStore;
    }

    @Override
    public BlobObject save(InputStream inputStream, String fileName, long length, BlobObject blob) {
        HibernateBlob o;
        if (blob==null) {
            // must create the instance...
            o = new HibernateBlob();
            o.setFileName(fileName);
            o.setBlobData(hbStore.getSession().getLobHelper().createBlob(inputStream, length));
        } else {
            // update the blob object
            o = (HibernateBlob)blob;
            o.setFileName(fileName);
            o.setBlobData(hbStore.getSession().getLobHelper().createBlob(inputStream, length));
        }
        hbStore.save(o);
        return o;
    }

    @Override
    public BlobObject getBlob(Long id) {
        return (HibernateBlob)hbStore.getSession().get(HibernateBlob.class, id);
    }
}

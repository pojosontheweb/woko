package woko.ext.blobs.hibernate

import junit.framework.TestCase
import woko.ext.blobs.BlobObject
import woko.ext.blobs.BlobStore
import woko.hibernate.HibernateStore
import woko.persistence.TransactionCallback

class HibernateBlobStoreTest extends TestCase {

    private HibernateStore store

    @Override
    protected void setUp() {
        store = new HibernateStore(['woko.ext.blobs.hibernate'])
    }

    @Override
    protected void tearDown() throws Exception {
        store.close()
    }

    private void doInTx(Closure c) {
        store.doInTransaction({
            c()
        } as TransactionCallback)
    }

    void testBlobStore() {
        Long id = null
        BlobStore bs = new HibernateBlobStore(store)
        String content = "thisisfunky"
        doInTx {

            int contentLen = content.length()

            InputStream is = new ByteArrayInputStream(content.bytes)
            BlobObject newBlob = bs.save(is, "test.txt", "text/plain", contentLen, new HibernateBlobImpl())
            assert newBlob
            assert newBlob.contentLength == contentLen
            assert newBlob.contentType == "text/plain"
            assert newBlob.fileName == "test.txt"
            InputStream is2 = newBlob.inputStream
            ByteArrayOutputStream os = new ByteArrayOutputStream()
            os << is2
            def s = os.toString()
            assert s == content

            id = newBlob.id
        }
        assert id
        doInTx {

            BlobObject b = bs.getBlob(HibernateBlobImpl.class, id)
            assert b
            InputStream is = b.inputStream
            ByteArrayOutputStream os = new ByteArrayOutputStream()
            os << is
            def s = os.toString()
            assert s == content

        }
    }

}

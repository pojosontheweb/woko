package woko.ext.blobs;

import java.io.InputStream;

public interface BlobStore {

    BlobObject save(InputStream inputStream, String fileName, long length, BlobObject blob);

    BlobObject getBlob(Long id);

}

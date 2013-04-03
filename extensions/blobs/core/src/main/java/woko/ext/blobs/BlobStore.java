package woko.ext.blobs;

import java.io.InputStream;

public interface BlobStore {

    String KEY = "BlobStore";

    BlobObject save(InputStream inputStream, String fileName, String contentType, long length, BlobObject blob);

    BlobObject getBlob(Long id);

}

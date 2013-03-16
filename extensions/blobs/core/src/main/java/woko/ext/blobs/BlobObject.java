package woko.ext.blobs;

import java.io.InputStream;

public interface BlobObject {

    Long getId();

    String getFileName();

    long getContentLength();

    String getContentType();

    InputStream getInputStream();

}

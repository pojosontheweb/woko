package woko.ext.blobs.hibernate;

import woko.ext.blobs.BlobObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

public interface HibernateBlob extends BlobObject {

    void setFileName(String fileName);

    void setContentType(String contentType);

    public Blob getBlobData();

    public void setBlobData(Blob blobData);

}

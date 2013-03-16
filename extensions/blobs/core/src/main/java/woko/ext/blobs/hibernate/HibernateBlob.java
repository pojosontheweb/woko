package woko.ext.blobs.hibernate;

import woko.ext.blobs.BlobObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;


@Entity
public class HibernateBlob implements BlobObject {

    @Id @GeneratedValue
    private Long id;

    @NotNull
    private String fileName;

    private String contentType;

    private Blob blobData;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Blob getBlobData() {
        return blobData;
    }

    public void setBlobData(Blob blobData) {
        this.blobData = blobData;
    }

    @Override
    public long getContentLength() {
        if (this.blobData!=null) {
            try {
                return blobData.length();
            } catch(SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            return -1;
        }
    }

    @Override
    public InputStream getInputStream() {
        try {
            return blobData.getBinaryStream();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

package in.clouthink.daas.fss.s3.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import in.clouthink.daas.fss.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class S3ObjectProxy {

    private String bucketName;

    private String objectKey;

    private AmazonS3 s3Client;

    private S3Object s3Object;

    public S3ObjectProxy(AmazonS3 s3Client, String bucketName, String objectKey) {
        this.bucketName = bucketName;
        this.objectKey = objectKey;
        this.s3Client = s3Client;
    }

    public S3ObjectProxy(S3Object s3Object) {
        this.s3Object = s3Object;
    }

    public void writeTo(OutputStream outputStream, int bufferSize) throws IOException {
        if (s3Object == null) {
            s3Object = s3Client.getObject(bucketName, objectKey);
        }

        InputStream is = s3Object.getObjectContent();
        try {
            IOUtils.copy(is, outputStream, bufferSize);
        } finally {
            IOUtils.close(is);
        }
    }

}

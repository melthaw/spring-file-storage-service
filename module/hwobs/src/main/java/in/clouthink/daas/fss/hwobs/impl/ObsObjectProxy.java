package in.clouthink.daas.fss.hwobs.impl;

import com.obs.services.ObsClient;
import com.obs.services.model.ObsObject;
import in.clouthink.daas.fss.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ObsObjectProxy {

    private String bucketName;

    private String objectName;

    private ObsClient obsClient;

    private ObsObject obsObject;

    public ObsObjectProxy(ObsClient obsClient, String bucketName, String objectName) {
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.obsClient = obsClient;
    }

    public ObsObjectProxy(ObsObject obsObject) {
        this.obsObject = obsObject;
    }

    public void writeTo(OutputStream outputStream, int bufferSize) throws IOException {
        if (obsObject == null) {
            obsObject = obsClient.getObject(bucketName, objectName);
        }

        InputStream is = obsObject.getObjectContent();
        try {
            IOUtils.copy(is, outputStream, bufferSize);
        } finally {
            IOUtils.close(is);
        }
    }

}

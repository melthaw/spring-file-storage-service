package in.clouthink.daas.fss.alioss.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import in.clouthink.daas.fss.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class OssObjectProxy {

    private String bucketName;

    private String objectName;

    private OSSClient ossClient;

    private OSSObject ossObject;

    public OssObjectProxy(OSSClient ossClient, String bucketName, String objectName) {
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.ossClient = ossClient;
    }

    public OssObjectProxy(OSSObject ossObject) {
        this.ossObject = ossObject;
    }

    public void writeTo(OutputStream outputStream, int bufferSize) throws IOException {
        if (ossObject == null) {
            ossObject = ossClient.getObject(bucketName, objectName);
        }

        InputStream is = ossObject.getObjectContent();
        try {
            IOUtils.copy(is, outputStream, bufferSize);
        } finally {
            IOUtils.close(is);
        }
    }

}

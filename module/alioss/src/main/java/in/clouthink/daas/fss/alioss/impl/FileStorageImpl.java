package in.clouthink.daas.fss.alioss.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import in.clouthink.daas.fss.alioss.support.OssFileProvider;
import in.clouthink.daas.fss.core.FileObject;
import in.clouthink.daas.fss.core.FileStorage;
import in.clouthink.daas.fss.util.IOUtils;

/**
 * Created by LiangBin on 16/4/16.
 */
public class FileStorageImpl implements FileStorage {
    
    private FileObject fileObject;
    
    private OssFileProvider ossFileProvider;
    
    public FileStorageImpl(FileObject fileObject,
                           OssFileProvider ossFileProvider) {
        this.fileObject = fileObject;
        this.ossFileProvider = ossFileProvider;
    }
    
    @Override
    public FileObject getFileObject() {
        return fileObject;
    }
    
    @Override
    public OssFileProvider getImplementation() {
        return ossFileProvider;
    }
    
    @Override
    public void writeTo(OutputStream outputStream,
                        long bufferSize) throws IOException {
        if (bufferSize <= 0) {
            bufferSize = 1024 * 4;
        }
        InputStream is = ossFileProvider.getOssObject(fileObject)
                                        .getObjectContent();
        int length = -1;
        byte[] bytes = new byte[(int) bufferSize];
        while ((length = is.read(bytes)) > 0) {
            outputStream.write(bytes, 0, length);
        }
        IOUtils.close(is);
    }
}

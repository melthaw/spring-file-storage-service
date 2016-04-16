package in.clouthink.daas.fss.alioss.impl;

import in.clouthink.daas.fss.alioss.support.OssFileProvider;
import in.clouthink.daas.fss.core.FileObject;
import in.clouthink.daas.fss.core.FileStorage;

import java.io.IOException;
import java.io.OutputStream;

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
    public Object getImplementation() {
        return ossFileProvider;
    }
    
    @Override
    public void writeTo(OutputStream outputStream,
                        long bufferSize) throws IOException {
        // TODO
    }
}

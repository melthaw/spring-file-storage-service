package in.clouthink.daas.fss.alioss.impl;

import java.io.InputStream;
import java.util.Date;

import in.clouthink.daas.fss.spi.FileObjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;

import in.clouthink.daas.fss.alioss.model.DefaultFileStorageMetadata;
import in.clouthink.daas.fss.alioss.model.OssFileObject;
import in.clouthink.daas.fss.alioss.support.OssFileProvider;
import in.clouthink.daas.fss.alioss.support.OssStrategy;
import in.clouthink.daas.fss.core.*;
import in.clouthink.daas.fss.spi.FileStorageService;

/**
 * Created by LiangBin on 16/4/16.
 */
public class FileStorageServiceImpl implements FileStorageService {
    
    private final DefaultFileStorageMetadata metadata = new DefaultFileStorageMetadata();
    
    @Autowired
    private OssStrategy strategy;
    
    @Autowired
    private OSSClient client;
    
    @Autowired
    private OssFileProvider ossFileProvider;
    
    @Autowired
    private FileObjectService fileObjectService;
    
    @Override
    public FileStorageMetadata getStorageMetadata() {
        return metadata;
    }
    
    @Override
    public FileStorage store(InputStream inputStream,
                             FileStorageRequest request) {
        OssFileObject fileObject = storeToOss(inputStream, request, null);
        
        return new FileStorageImpl(fileObject, ossFileProvider);
    }
    
    private OssFileObject storeToOss(InputStream inputStream,
                                     FileStorageRequest request,
                                     String key) {
        OssFileObject fileObject = checkAndCreateFileObject(request);
        
        if (StringUtils.isEmpty(key)) {
            key = strategy.generateKey(fileObject);
        }
        String bucket = strategy.getBucket(fileObject);
        fileObject.setFinalFilename(key);
        ObjectMetadata objectMetadata = strategy.createObjectMeta(fileObject);
        client.putObject(bucket, key, inputStream, objectMetadata);
        fileObject.setId(key);
        return fileObject;
    }
    
    private OssFileObject checkAndCreateFileObject(FileStorageRequest request) {
        OssFileObject fileObject = copy(request);
        if (StringUtils.isEmpty(fileObject.getOriginalFilename())) {
            throw new FileStorageException("The originalFilename is required.");
        }
        if (StringUtils.isEmpty(fileObject.getUploadedBy())) {
            throw new FileStorageException("The uploadedBy is required.");
        }
        if (fileObject.getUploadedAt() == null) {
            fileObject.setUploadedAt(new Date());
        }
        if (StringUtils.isEmpty(fileObject.getPrettyFilename())) {
            fileObject.setPrettyFilename(fileObject.getOriginalFilename());
        }
        if (fileObject.getVersion() == 0) {
            fileObject.setVersion(1);
        }
        fileObject.setUploadedAt(new Date());
        return fileObject;
    }
    
    @Override
    public FileStorage restore(String previousId,
                               InputStream inputStream,
                               FileStorageRequest request) {
        FileObject fileObject = fileObjectService.findById(previousId);
        if (fileObject != null) {
            ((OssFileObject) fileObject).setVersion(fileObject.getVersion()
                                                    + 1);
        }
        FileObject newFileObject = storeToOss(inputStream,
                                              fileObject,
                                              previousId);
        return new FileStorageImpl(newFileObject, ossFileProvider);
    }
    
    @Override
    public FileStorage findById(String id) {
        FileObject fileObject = fileObjectService.findById(id);
        if (fileObject == null) {
            return null;
        }
        return new FileStorageImpl(fileObject, ossFileProvider);
    }
    
    @Override
    public FileStorage findByFilename(String filename) {
        return findById(filename);
    }
    
    private OssFileObject copy(FileStorageRequest request) {
        OssFileObject fileObject = new OssFileObject();
        BeanUtils.copyProperties(request, fileObject);
        return fileObject;
    }
    
}

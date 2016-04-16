package in.clouthink.daas.fss.alioss.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;

import in.clouthink.daas.fss.alioss.support.OssStrategy;
import in.clouthink.daas.fss.core.FileObject;
import in.clouthink.daas.fss.core.FileObjectHistory;
import in.clouthink.daas.fss.core.FileStorageException;
import in.clouthink.daas.fss.spi.FileObjectService;

/**
 * Created by LiangBin on 16/4/16.
 */
public class FileObjectServiceImpl implements FileObjectService {
    
    @Autowired
    private OSSClient client;
    
    @Autowired
    private OssStrategy strategy;
    
    @Override
    public FileObject save(FileObject fileObject) {
        // TODO whether is this needed?
        return fileObject;
    }
    
    @Override
    public FileObject findById(String id) {
        String bucket = strategy.getBucket(id);
        if (StringUtils.isBlank(bucket)) {
            throw new FileStorageException("Can't resolve OSS bucket.");
        }
        
        ObjectMetadata objectMetadata = client.getObjectMetadata(bucket, id);
        if (objectMetadata == null) {
            return null;
        }
        return strategy.revertFromObjectMeta(objectMetadata, id);
    }
    
    @Override
    public FileObject findByFinalFilename(String finalFileName) {
        return findById(finalFileName);
    }
    
    @Override
    public FileObject deleteById(String id) {
        // TODO whether is this needed?
        FileObject fileObject = findById(id);
        if (fileObject != null) {
            String bucket = strategy.getBucket(id);
            client.deleteObject(bucket, id);
        }
        
        return fileObject;
    }
    
    @Override
    public FileObject deleteByFinalFilename(String finalFileName) {
        return deleteById(finalFileName);
    }
    
    @Override
    public FileObjectHistory saveAsHistory(FileObject fileObject) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public List<FileObjectHistory> findHistoryById(String fileObjectId) {
        throw new UnsupportedOperationException();
    }
}

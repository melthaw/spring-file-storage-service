package in.clouthink.daas.fss.alioss.support;

import com.aliyun.oss.model.ObjectMetadata;
import in.clouthink.daas.fss.core.FileObject;
import in.clouthink.daas.fss.core.FileStorageRequest;

/**
 * Created by LiangBin on 16/4/16.
 */
public interface OssStrategy {
    
    /**
     * Generate the unique key (unique file name is OSS)
     * 
     * @param request
     * @return
     */
    String generateKey(FileStorageRequest request);
    
    /**
     * Get the bucket that the file requested will store
     * 
     * @param request
     * @return
     */
    String getBucket(FileStorageRequest request);
    
    /**
     * Get the bucket that the file with the given key stored<br/>
     * If there is only one bucket, then set bucket in OssConfig,<br/>
     * otherwise the app should record file key, bucket mappings.
     * 
     * @param key
     *            OSS file key
     * @return
     */
    String getBucket(String key);
    
    /**
     * Create an object meta from a FileStorageRequest
     * 
     * @param request
     * @return
     */
    ObjectMetadata createObjectMeta(FileStorageRequest request);
    
    /**
     * Revert a FileObject from object metadata
     * 
     * @param metadata
     * @return
     */
    FileObject revertFromObjectMeta(ObjectMetadata metadata, String key);
}

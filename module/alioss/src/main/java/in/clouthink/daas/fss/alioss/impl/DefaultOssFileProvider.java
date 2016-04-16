package in.clouthink.daas.fss.alioss.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import in.clouthink.daas.fss.alioss.support.OssFileProvider;
import in.clouthink.daas.fss.alioss.support.OssStrategy;
import in.clouthink.daas.fss.core.FileObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by LiangBin on 16/4/16.
 */
public class DefaultOssFileProvider implements OssFileProvider {
    
    @Autowired
    private OSSClient client;
    
    @Autowired
    private OssStrategy strategy;
    
    @Override
    public OSSObject getOssObject(FileObject fileObject) {
        String bucket = strategy.getBucket(fileObject);
        return client.getObject(bucket, fileObject.getId());
    }
}

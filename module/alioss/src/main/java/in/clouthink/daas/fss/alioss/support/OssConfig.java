package in.clouthink.daas.fss.alioss.support;

/**
 * Config for oss client. Created by LiangBin on 16/4/16.
 */
public interface OssConfig {
    
    String getEndpoint();
    
    String getKeyId();
    
    String getSecret();
    
    String getDefaultBucket();
}

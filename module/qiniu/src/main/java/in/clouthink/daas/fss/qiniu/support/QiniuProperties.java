package in.clouthink.daas.fss.qiniu.support;

import java.util.Map;

/**
 * Config for qiniu client.
 *
 * @author dz
 * @since 3
 */
public interface QiniuProperties {

    /**
     * The host of qiniu cloud
     */
    String getHost();

    /**
     * The access key
     */
    String getAccessKey();

    /**
     * The secret key
     */
    String getSecretKey();

    /**
     * The default bucket to store the file which's bucket is not specified for category.
     */
    String getDefaultBucket();

    /**
     * The file's category and bucket mapping, the file will be stored in corresponding bucket for different category,
     * if the bucket is not defined for the file's category , the default bucket will be taken place.
     */
    Map<String, String> getBuckets();
}

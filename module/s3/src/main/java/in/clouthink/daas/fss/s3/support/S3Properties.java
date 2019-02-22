package in.clouthink.daas.fss.s3.support;

import java.util.Map;

/**
 * Config for ceph S3 client.
 *
 * @author dz
 */
public interface S3Properties {

    /**
     * The s3 access key
     */
    String getAccessKey();

    /**
     * The s3 access secret
     */
    String getSecretKey();

    /**
     * The s3 endpoint
     */
    String getEndpoint();

//    /**
//     * The access protocol
//     */
//    String getProtocol();

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

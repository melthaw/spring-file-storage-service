package in.clouthink.daas.fss.s3.support;

import com.amazonaws.ClientConfiguration;

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
     * The service endpoint either with or without the protocol (e.g. https://sns.us-west-1.amazonaws.com or sns.us-west-1.amazonaws.com)
     */
    String getEndpoint();

    /**
     * The region to use for SigV4 signing of requests (e.g. us-west-1)
     */
    String getRegion();

    /**
     * The bucket style
     * <p>
     * host for virtual-host as default
     * <p>
     * path for sub path
     */
    String getBucketStyle();

    /**
     * The default bucket to store the file which's bucket is not specified for category.
     */
    String getDefaultBucket();

    /**
     * The file's category and bucket mapping, the file will be stored in corresponding bucket for different category,
     * if the bucket is not defined for the file's category , the default bucket will be taken place.
     */
    Map<String, String> getBuckets();

    /**
     * Client configuration options such as proxy settings, user agent string, max retry attempts, etc.
     */
    ClientConfiguration getClientConfiguration();
}

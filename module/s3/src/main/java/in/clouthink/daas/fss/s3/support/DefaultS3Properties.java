package in.clouthink.daas.fss.s3.support;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dz
 */
public class DefaultS3Properties implements S3Properties, InitializingBean {

	private String accessKey;

	private String secretKey;

	private String endpoint;

	private String region;

	private String bucketStyle = "host";

	private String defaultBucket;

	private Map<String,String> buckets = new HashMap<String,String>();

	@Override
	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	@Override
	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	@Override
	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	@Override
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Override
	public String getBucketStyle() {
		return bucketStyle;
	}

	public void setBucketStyle(String bucketStyle) {
		this.bucketStyle = bucketStyle;
	}

	@Override
	public String getDefaultBucket() {
		return defaultBucket;
	}

	public void setDefaultBucket(String defaultBucket) {
		this.defaultBucket = defaultBucket;
	}

	@Override
	public Map<String,String> getBuckets() {
		return buckets;
	}

	public void setBuckets(Map<String,String> buckets) {
		this.buckets = buckets;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.accessKey);
		Assert.notNull(this.secretKey);
		Assert.notNull(this.endpoint);
		Assert.notNull(this.defaultBucket);
	}

}

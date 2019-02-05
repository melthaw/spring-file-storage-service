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

	private String protocol;

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
	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
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
		Assert.notNull(accessKey);
		Assert.notNull(secretKey);
		Assert.notNull(endpoint);
		Assert.notNull(protocol);
		Assert.notNull(defaultBucket);
	}

}

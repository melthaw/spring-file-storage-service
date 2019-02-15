package in.clouthink.daas.fss.alioss.support;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dz
 */
public class DefaultOssProperties implements OssProperties, InitializingBean {

	private String keyId;

	private String keySecret;

	private String endpoint;

	private String defaultBucket;

	private Map<String,String> buckets = new HashMap<String,String>();

	@Override
	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	@Override
	public String getKeySecret() {
		return keySecret;
	}

	public void setKeySecret(String keySecret) {
		this.keySecret = keySecret;
	}

	@Override
	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
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
		Assert.notNull(keyId);
		Assert.notNull(keySecret);
		Assert.notNull(endpoint);
		Assert.notNull(defaultBucket);
	}

}

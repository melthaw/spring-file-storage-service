package in.clouthink.daas.fss.alioss.support.impl;

import in.clouthink.daas.fss.alioss.support.OssProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dz
 */
public class DefaultOssProperties implements OssProperties, InitializingBean {

	private String keyId;

	private String secret;

	private String ossDomain;

	private String imgDomain;

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
	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	@Override
	public String getOssDomain() {
		return ossDomain;
	}

	public void setOssDomain(String ossDomain) {
		this.ossDomain = ossDomain;
	}

	@Override
	public String getImgDomain() {
		return imgDomain;
	}

	public void setImgDomain(String imgDomain) {
		this.imgDomain = imgDomain;
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
		Assert.notNull(secret);
		Assert.notNull(ossDomain);
		Assert.notNull(imgDomain);
		Assert.notNull(defaultBucket);
	}

}

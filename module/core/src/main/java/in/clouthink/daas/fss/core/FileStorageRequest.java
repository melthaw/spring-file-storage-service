package in.clouthink.daas.fss.core;

import java.util.Map;

/**
 * Created by dz on 16/3/29.
 */
public interface FileStorageRequest {

	/**
	 * for example :  android, ad, album, etc. we suggest to organize the file object by biz category
	 *
	 * @return
	 */
	String getCategory();

	String getFinalFilename();

	String getOriginalFilename();

	String getPrettyFilename();

	String getContentType();

	String getUploadedBy();

	String getCode();

	String getBizId();

	Map<String, String> getAttributes();

}

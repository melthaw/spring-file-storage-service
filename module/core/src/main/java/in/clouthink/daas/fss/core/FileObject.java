package in.clouthink.daas.fss.core;

import java.util.Date;
import java.util.Map;

/**
 * Created by dz on 16/3/28.
 */
public interface FileObject {

	String getId();

	String getFinalFilename();

	String getOriginalFilename();

	String getPrettyFilename();

	String getContentType();

	String getUploadedBy();

	Date getUploadedAt();

	int getVersion();

	String getCode();

	String getBizId();

	Map<String, String> getAttributes();

}

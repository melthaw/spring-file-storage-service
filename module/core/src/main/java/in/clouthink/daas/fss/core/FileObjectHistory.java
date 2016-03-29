package in.clouthink.daas.fss.core;

import java.util.Date;

/**
 * Created by dz on 16/3/28.
 */
public interface FileObjectHistory {

	String getId();

	FileObject getFileObject();

	String getFinalFilename();

	String getOriginalFilename();

	String getPrettyFilename();

	String getContentType();

	String getUploadedBy();

	Date getUploadedAt();

	int getVersion();

}

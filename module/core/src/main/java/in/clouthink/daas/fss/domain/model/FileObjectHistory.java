package in.clouthink.daas.fss.domain.model;

import java.util.Date;
import java.util.Map;

/**
 * The file object update history (version management).
 *
 * @author dz
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

	long getSize();

	int getVersion();

	Map<String,String> getAttributes();

}

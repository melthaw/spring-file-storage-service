package in.clouthink.daas.fss.core;

import java.util.Date;
import java.util.Map;

/**
 * Created by dz on 16/3/28.
 */
public interface FileObject extends FileStorageRequest {

	String getId();

	Date getUploadedAt();

	int getVersion();

}

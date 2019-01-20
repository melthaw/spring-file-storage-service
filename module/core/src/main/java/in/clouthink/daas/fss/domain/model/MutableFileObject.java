package in.clouthink.daas.fss.domain.model;

import java.util.Date;

/**
 * @author dz
 */
public interface MutableFileObject extends FileObject {

	void setStoredFilename(String storedFilename);

	void setUploadedAt(Date uploadedAt);

	void setVersion(int version);

}

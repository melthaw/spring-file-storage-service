package in.clouthink.daas.fss.core;

import java.util.Date;

/**
 * @author dz
 */
public interface MutableFileObject extends FileObject {

	void setFinalFilename(String finalFilename);

	void setUploadedAt(Date uploadedAt);

	void setVersion(int version);

}

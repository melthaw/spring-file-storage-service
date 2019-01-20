package in.clouthink.daas.fss.core;

import in.clouthink.daas.fss.domain.model.FileObject;

import java.util.Date;

/**
 * @author dz
 */
public interface MutableFileObject extends FileObject {

	void setFinalFilename(String finalFilename);

	void setUploadedAt(Date uploadedAt);

	void setVersion(int version);

}

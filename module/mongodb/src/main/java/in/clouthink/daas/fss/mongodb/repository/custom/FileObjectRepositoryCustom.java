package in.clouthink.daas.fss.mongodb.repository.custom;

import in.clouthink.daas.fss.mongodb.model.FileObject;
import in.clouthink.daas.fss.mongodb.model.FileObjectSearchRequest;
import org.springframework.data.domain.Page;

/**
 * @author dz
 */
public interface FileObjectRepositoryCustom {

	Page<FileObject> findPage(FileObjectSearchRequest searchRequest);

}

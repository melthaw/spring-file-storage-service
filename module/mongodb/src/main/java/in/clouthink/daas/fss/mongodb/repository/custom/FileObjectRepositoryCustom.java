package in.clouthink.daas.fss.mongodb.repository.custom;

import in.clouthink.daas.fss.domain.request.FileObjectSearchRequest;
import in.clouthink.daas.fss.mongodb.model.FileObject;
import org.springframework.data.domain.Page;

/**
 * @author dz
 */
public interface FileObjectRepositoryCustom {

	Page<FileObject> findPage(FileObjectSearchRequest searchRequest);

}

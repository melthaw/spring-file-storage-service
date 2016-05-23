package in.clouthink.daas.fss.mongodb.repository.custom;

import in.clouthink.daas.fss.mongodb.model.FileObject;
import in.clouthink.daas.fss.mongodb.repository.FileObjectQueryParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 */
public interface FileObjectRepositoryCustom {

	Page<FileObject> findPage(FileObjectQueryParameter queryParameter, Pageable pageable);

}

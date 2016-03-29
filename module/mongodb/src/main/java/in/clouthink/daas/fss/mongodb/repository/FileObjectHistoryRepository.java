package in.clouthink.daas.fss.mongodb.repository;

import in.clouthink.daas.fss.mongodb.model.FileObject;
import in.clouthink.daas.fss.mongodb.model.FileObjectHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by dz on 16/3/29.
 */
public interface FileObjectHistoryRepository extends AbstractRepository<FileObjectHistory> {

	Page<FileObjectHistory> findByFileObject(FileObject fileObject, Pageable pageable);

	List<FileObjectHistory> findByFileObject(FileObject fileObject);

}

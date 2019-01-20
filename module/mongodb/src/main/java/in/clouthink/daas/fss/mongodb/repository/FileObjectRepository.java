package in.clouthink.daas.fss.mongodb.repository;

import in.clouthink.daas.fss.mongodb.model.FileObject;
import in.clouthink.daas.fss.mongodb.repository.custom.FileObjectRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

/**
 * @author dz
 */
public interface FileObjectRepository extends AbstractRepository<FileObject>, FileObjectRepositoryCustom {

    FileObject findByFinalFilename(String finalFilename);

    Page<FileObject> findByCategory(String category, Pageable pageable);

    Page<FileObject> findByUploadedBy(String uploadedBy, Pageable pageable);

    Page<FileObject> findByUploadedAtBetween(Date start, Date end, Pageable pageable);

    Page<FileObject> findByBizId(String bizId, Pageable pageable);

}

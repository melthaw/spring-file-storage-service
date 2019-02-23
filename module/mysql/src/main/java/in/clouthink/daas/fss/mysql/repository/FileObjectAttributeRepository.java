package in.clouthink.daas.fss.mysql.repository;

import in.clouthink.daas.fss.mysql.model.FileObject;
import in.clouthink.daas.fss.mysql.model.FileObjectAttribute;
import in.clouthink.daas.fss.mysql.repository.custom.FileObjectRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * @author dz
 */
public interface FileObjectAttributeRepository extends AbstractRepository<FileObjectAttribute> {

    List<FileObjectAttribute> findListByFileObject(FileObject fileObject);

    void deleteByFileObjectId(String id);

    void deleteByFileObject(FileObject result);
}

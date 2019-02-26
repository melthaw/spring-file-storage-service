package in.clouthink.daas.fss.mysql.repository.custom;

import in.clouthink.daas.fss.domain.request.FileObjectSearchRequest;
import in.clouthink.daas.fss.mysql.model.FileObject;
import org.springframework.data.domain.Page;

/**
 * @author dz
 */
public interface FileObjectRepositoryCustom {

    Page<FileObject> findPage(FileObjectSearchRequest searchRequest);

}

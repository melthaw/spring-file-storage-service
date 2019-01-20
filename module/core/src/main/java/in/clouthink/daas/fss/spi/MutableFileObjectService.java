package in.clouthink.daas.fss.spi;

import in.clouthink.daas.fss.domain.model.FileObject;
import in.clouthink.daas.fss.domain.model.FileObjectHistory;
import in.clouthink.daas.fss.core.StoreFileRequest;
import in.clouthink.daas.fss.domain.service.FileObjectService;

/**
 * @author dz on 16/7/11.
 */
public interface MutableFileObjectService extends FileObjectService {

	FileObject merge(StoreFileRequest request, FileObject fileObject);

	FileObject save(FileObject fileObject);

	FileObject deleteById(String id);

	FileObjectHistory saveAsHistory(FileObject fileObject);

}

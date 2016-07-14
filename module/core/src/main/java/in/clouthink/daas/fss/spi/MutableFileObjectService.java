package in.clouthink.daas.fss.spi;

import in.clouthink.daas.fss.core.FileObject;
import in.clouthink.daas.fss.core.FileObjectHistory;
import in.clouthink.daas.fss.core.FileStorageRequest;

/**
 * @author dz on 16/7/11.
 */
public interface MutableFileObjectService extends FileObjectService {

	FileObject merge(FileStorageRequest request, FileObject fileObject);

	FileObject save(FileObject fileObject);

	FileObject deleteById(String id);

	FileObjectHistory saveAsHistory(FileObject fileObject);

}

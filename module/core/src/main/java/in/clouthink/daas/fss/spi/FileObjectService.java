package in.clouthink.daas.fss.spi;

import in.clouthink.daas.fss.core.FileObject;
import in.clouthink.daas.fss.core.FileObjectHistory;

import java.util.List;

/**
 * The file object and the associated attributes is stored in the file storage server.
 * To improve the file object(not physical object) access performance,
 * we need the file object service which save the file object attributes on the local database.
 * <p>
* @author dz on 16/3/28.
 */
public interface FileObjectService {

	FileObject findById(String id);

	FileObject findByFinalFilename(String finalFileName);

	List<FileObjectHistory> findHistoryById(String id);

}

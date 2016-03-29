package in.clouthink.daas.fss.core;

import java.util.List;

/**
 * Created by dz on 16/3/28.
 */
public interface FileObjectService {

	FileObject save(FileObject fileObject);

	FileObject findById(String id);

	FileObject findByFinalFilename(String finalFileName);

	FileObject deleteById(String id);

	FileObject deleteByFinalFilename(String finalFileName);

	FileObjectHistory saveAsHistory(FileObject fileObject);

	List<FileObjectHistory> findHistoryById(String fileObjectId);

}

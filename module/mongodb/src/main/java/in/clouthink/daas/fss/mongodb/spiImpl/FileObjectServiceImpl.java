package in.clouthink.daas.fss.mongodb.spiImpl;

import in.clouthink.daas.edm.Edms;
import in.clouthink.daas.fss.domain.model.FileObject;
import in.clouthink.daas.fss.domain.model.FileObjectHistory;
import in.clouthink.daas.fss.core.StoreFileException;
import in.clouthink.daas.fss.core.StoreFileRequest;
import in.clouthink.daas.fss.domain.service.FileObjectService;
import in.clouthink.daas.fss.mongodb.repository.FileObjectHistoryRepository;
import in.clouthink.daas.fss.mongodb.repository.FileObjectRepository;
import in.clouthink.daas.fss.spi.MutableFileObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author dz
 */
public class FileObjectServiceImpl implements FileObjectService {

	@Autowired
	private FileObjectRepository fileObjectRepository;

	@Autowired
	private FileObjectHistoryRepository fileObjectHistoryRepository;

	@Override
	public FileObject merge(StoreFileRequest request, FileObject fileObject) {
		if (StringUtils.isEmpty(request.getOriginalFilename())) {
			throw new StoreFileException("The originalFilename is required.");
		}
		if (StringUtils.isEmpty(request.getUploadedBy())) {
			throw new StoreFileException("The uploadedBy is required.");
		}

		in.clouthink.daas.fss.mongodb.model.FileObject fileObjectImpl = (in.clouthink.daas.fss.mongodb.model.FileObject) fileObject;
		//only the file part is merged
		fileObjectImpl.setOriginalFilename(request.getOriginalFilename());
		fileObjectImpl.setPrettyFilename(request.getPrettyFilename());
		fileObjectImpl.setContentType(request.getContentType());
		fileObjectImpl.setSize(request.getSize());
		fileObjectImpl.setUploadedBy(request.getUploadedBy());
		fileObjectImpl.setUploadedAt(new Date());
		if (StringUtils.isEmpty(fileObjectImpl.getPrettyFilename())) {
			fileObjectImpl.setPrettyFilename(fileObjectImpl.getOriginalFilename());
		}
		if (fileObjectImpl.getVersion() == 0) {
			fileObjectImpl.setVersion(1);
		}
		else {
			fileObjectImpl.setVersion(fileObjectImpl.getVersion() + 1);
		}

		return fileObjectImpl;
	}

	@Override
	public FileObject save(FileObject fileObject) {
		if (StringUtils.isEmpty(fileObject.getOriginalFilename())) {
			throw new StoreFileException("The originalFilename is required.");
		}
		if (StringUtils.isEmpty(fileObject.getUploadedBy())) {
			throw new StoreFileException("The uploadedBy is required.");
		}

		in.clouthink.daas.fss.mongodb.model.FileObject fileObjectImpl = null;
		if (fileObject instanceof in.clouthink.daas.fss.mongodb.model.FileObject) {
			fileObjectImpl = (in.clouthink.daas.fss.mongodb.model.FileObject) fileObject;
		}
		else {
			fileObjectImpl = in.clouthink.daas.fss.mongodb.model.FileObject.from(fileObject);
		}

		if (fileObjectImpl.getUploadedAt() == null) {
			fileObjectImpl.setUploadedAt(new Date());
		}
		if (StringUtils.isEmpty(fileObjectImpl.getPrettyFilename())) {
			fileObjectImpl.setPrettyFilename(fileObjectImpl.getOriginalFilename());
		}
		if (fileObjectImpl.getVersion() == 0) {
			fileObjectImpl.setVersion(1);
		}
		return fileObjectRepository.save(fileObjectImpl);
	}

	@Override
	public FileObject findById(String id) {
		return fileObjectRepository.findById(id);
	}

	@Override
	public FileObject findByStoredFilename(String storedFileName) {
		return fileObjectRepository.findByFinalFilename(storedFileName);
	}

	@Override
	public FileObject deleteById(String id) {
		in.clouthink.daas.fss.mongodb.model.FileObject result = fileObjectRepository.findById(id);
		if (result != null) {
			fileObjectRepository.delete(result);
			Edms.getEdm().dispatch("in.clouthink.daas.fss#delete", result);
		}
		return result;
	}

	@Override
	public FileObjectHistory saveAsHistory(FileObject fileObject) {
		in.clouthink.daas.fss.mongodb.model.FileObject fileObjectImpl = null;
		if (fileObject instanceof in.clouthink.daas.fss.mongodb.model.FileObject) {
			fileObjectImpl = (in.clouthink.daas.fss.mongodb.model.FileObject) fileObject;
		}
		else {
			fileObjectImpl = in.clouthink.daas.fss.mongodb.model.FileObject.from(fileObject);
		}

		in.clouthink.daas.fss.mongodb.model.FileObjectHistory result = in.clouthink.daas.fss.mongodb.model.FileObjectHistory
				.from(fileObjectImpl);
		return fileObjectHistoryRepository.save(result);
	}

	@Override
	public List<FileObjectHistory> findHistoryByFileObjectId(String fileObjectId) {
		in.clouthink.daas.fss.mongodb.model.FileObject fileObjectImpl = fileObjectRepository.findById(fileObjectId);
		List<FileObjectHistory> result = new ArrayList<FileObjectHistory>();
		result.addAll(fileObjectHistoryRepository.findByFileObject(fileObjectImpl));
		return result;
	}

}

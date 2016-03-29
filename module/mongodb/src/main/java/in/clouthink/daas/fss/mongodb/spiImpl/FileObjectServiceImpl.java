package in.clouthink.daas.fss.mongodb.spiImpl;

import in.clouthink.daas.fss.core.FileObject;
import in.clouthink.daas.fss.core.FileObjectHistory;
import in.clouthink.daas.fss.core.FileObjectService;
import in.clouthink.daas.fss.core.FileStorageException;
import in.clouthink.daas.fss.mongodb.repository.FileObjectHistoryRepository;
import in.clouthink.daas.fss.mongodb.repository.FileObjectRepository;
import in.clouthink.daas.fss.mongodb.service.FileObjectServiceExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dz on 16/3/29.
 */
public class FileObjectServiceImpl implements FileObjectService, FileObjectServiceExtension {

	@Autowired
	private FileObjectRepository fileObjectRepository;

	@Autowired
	private FileObjectHistoryRepository fileObjectHistoryRepository;

	@Override
	public FileObject save(FileObject fileObject) {
		in.clouthink.daas.fss.mongodb.model.FileObject fileObjectImpl = (in.clouthink.daas.fss.mongodb.model.FileObject) fileObject;

		if (StringUtils.isEmpty(fileObjectImpl.getOriginalFilename())) {
			throw new FileStorageException("The originalFilename is required.");
		}
		if (StringUtils.isEmpty(fileObjectImpl.getUploadedBy())) {
			throw new FileStorageException("The uploadedBy is required.");
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
	public FileObject findByFinalFilename(String finalFilename) {
		return fileObjectRepository.findByFinalFilename(finalFilename);
	}

	@Override
	public FileObject deleteById(String id) {
		in.clouthink.daas.fss.mongodb.model.FileObject result = fileObjectRepository.findById(id);
		if (result != null) {
			fileObjectRepository.delete(result);
		}
		return result;
	}

	@Override
	public FileObject deleteByFinalFilename(String finalFilename) {
		in.clouthink.daas.fss.mongodb.model.FileObject result = fileObjectRepository.findByFinalFilename(finalFilename);
		if (result != null) {
			fileObjectRepository.delete(result);
		}
		return result;
	}

	@Override
	public FileObjectHistory saveAsHistory(FileObject fileObject) {
		in.clouthink.daas.fss.mongodb.model.FileObject fileObjectImpl = (in.clouthink.daas.fss.mongodb.model.FileObject) fileObject;
		in.clouthink.daas.fss.mongodb.model.FileObjectHistory result = new in.clouthink.daas.fss.mongodb.model.FileObjectHistory();

		result.setFileObject(fileObjectImpl);
		result.setFinalFilename(fileObjectImpl.getFinalFilename());
		result.setOriginalFilename(fileObjectImpl.getOriginalFilename());
		result.setPrettyFilename(fileObjectImpl.getPrettyFilename());

		result.setContentType(fileObjectImpl.getContentType());
		result.setUploadedBy(fileObjectImpl.getUploadedBy());
		result.setUploadedAt(fileObjectImpl.getUploadedAt());
		result.setVersion(fileObjectImpl.getVersion());

		return fileObjectHistoryRepository.save(result);
	}

	@Override
	public List<FileObjectHistory> findHistoryById(String fileObjectId) {
		in.clouthink.daas.fss.mongodb.model.FileObject fileObjectImpl = fileObjectRepository.findById(fileObjectId);
		List<FileObjectHistory> result = new ArrayList<FileObjectHistory>();
		result.addAll(fileObjectHistoryRepository.findByFileObject(fileObjectImpl));
		return result;
	}
}

package in.clouthink.daas.fss.mongodb.spiImpl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import in.clouthink.daas.edm.Edms;
import in.clouthink.daas.edm.EventListener;
import in.clouthink.daas.fss.core.*;
import in.clouthink.daas.fss.core.FileObject;
import in.clouthink.daas.fss.mongodb.model.*;
import in.clouthink.daas.fss.mongodb.service.GridFSDBFileProvider;
import in.clouthink.daas.fss.mongodb.service.GridFSService;
import in.clouthink.daas.fss.spi.FileObjectService;
import in.clouthink.daas.fss.spi.FileStorageService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.Date;

/**
 * Created by dz on 16/3/29.
 */
public class FileStorageServiceImpl implements FileStorageService, EventListener<FileObject>, InitializingBean {

	private final DefaultFileStorageMetadata metadata = new DefaultFileStorageMetadata();

	@Autowired
	private FileObjectService fileObjectService;

	@Autowired
	private GridFSService gridFSService;

	@Override
	public FileStorageMetadata getStorageMetadata() {
		return metadata;
	}

	@Override
	public FileStorage store(InputStream inputStream, FileStorageRequest request) {
		final in.clouthink.daas.fss.mongodb.model.FileObject fileObject = new in.clouthink.daas.fss.mongodb.model.FileObject();
		copy(request, fileObject);

		GridFSInputFile gridFSInputFile = storeFile(inputStream, request);
		fileObjectService.save(fileObject);

		return new FileStorageImpl(fileObject, new GridFSDBFileProvider() {
			@Override
			public GridFSDBFile getGridFSDBFile() {
				return gridFSService.getGridFS().findOne(fileObject.getFinalFilename());
			}
		});
	}

	@Override
	public FileStorage restore(String id, InputStream inputStream, FileStorageRequest request) {
		if (StringUtils.isEmpty(id)) {
			throw new FileStorageException("The file object id is required.");
		}
		final in.clouthink.daas.fss.mongodb.model.FileObject fileObject = (in.clouthink.daas.fss.mongodb.model.FileObject) fileObjectService
				.findById(id);
		if (fileObject == null) {
			throw new FileStorageException("The file object of specified id is not found.");
		}

		GridFSInputFile gridFSInputFile = storeFile(inputStream, request);

		fileObjectService.saveAsHistory(fileObject);

		merge(request, fileObject);
		fileObjectService.save(fileObject);

		return new FileStorageImpl(fileObject, new GridFSDBFileProvider() {
			@Override
			public GridFSDBFile getGridFSDBFile() {
				return gridFSService.getGridFS().findOne(fileObject.getFinalFilename());
			}
		});
	}

	@Override
	public FileStorage findById(String id) {
		final FileObject fileObject = fileObjectService.findById(id);
		if (fileObject == null) {
			return null;
		}
		return new FileStorageImpl(fileObject, new GridFSDBFileProvider() {
			@Override
			public GridFSDBFile getGridFSDBFile() {
				return gridFSService.getGridFS().findOne(fileObject.getFinalFilename());
			}
		});
	}

	@Override
	public FileStorage findByFilename(String filename) {
		final FileObject fileObject = fileObjectService.findByFinalFilename(filename);
		if (fileObject == null) {
			return null;
		}
		return new FileStorageImpl(fileObject, new GridFSDBFileProvider() {
			@Override
			public GridFSDBFile getGridFSDBFile() {
				return gridFSService.getGridFS().findOne(fileObject.getFinalFilename());
			}
		});
	}

	@Override
	public void onEvent(FileObject fileObject) {
		gridFSService.getGridFS().remove(fileObject.getFinalFilename());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Edms.getEdm().register("in.clouthink.daas.fss#delete", this);
	}

	private void copy(FileStorageRequest request, in.clouthink.daas.fss.mongodb.model.FileObject fileObject) {
		fileObject.setCode(request.getCode());
		fileObject.setBizId(request.getBizId());
		fileObject.setCategory(request.getCategory());
		fileObject.setAttributes(request.getAttributes());
		fileObject.setOriginalFilename(request.getOriginalFilename());
		fileObject.setPrettyFilename(request.getPrettyFilename());
		fileObject.setFinalFilename(request.getFinalFilename());
		fileObject.setContentType(request.getContentType());
		fileObject.setUploadedBy(request.getUploadedBy());
		fileObject.setUploadedAt(new Date());
		fileObject.setVersion(1);
	}

	private void merge(FileStorageRequest request, in.clouthink.daas.fss.mongodb.model.FileObject fileObject) {
		if (!StringUtils.isEmpty(request.getCode())) {
			fileObject.setCode(request.getCode());
		}
		if (!StringUtils.isEmpty(request.getBizId())) {
			fileObject.setBizId(request.getBizId());
		}
		if (!StringUtils.isEmpty(request.getCategory())) {
			fileObject.setCategory(request.getCategory());
		}
		if (request.getAttributes() != null) {
			fileObject.getAttributes().putAll(request.getAttributes());
		}
		fileObject.setOriginalFilename(request.getOriginalFilename());
		fileObject.setPrettyFilename(request.getPrettyFilename());
		fileObject.setFinalFilename(request.getFinalFilename());
		fileObject.setContentType(request.getContentType());
		fileObject.setUploadedBy(request.getUploadedBy());
		fileObject.setUploadedAt(new Date());
		fileObject.setVersion(fileObject.getVersion() + 1);
	}

	private GridFSInputFile storeFile(InputStream inputStream, FileStorageRequest request) {
		GridFSInputFile gfsInputFile = gridFSService.getGridFS().createFile(inputStream);
		gfsInputFile.setFilename(request.getFinalFilename());
		gfsInputFile.setContentType(request.getContentType());
		if (request.getAttributes() != null) {
			DBObject dbObject = new BasicDBObject(request.getAttributes());
			gfsInputFile.setMetaData(dbObject);
		}
		gfsInputFile.save();
		return gfsInputFile;
	}

}

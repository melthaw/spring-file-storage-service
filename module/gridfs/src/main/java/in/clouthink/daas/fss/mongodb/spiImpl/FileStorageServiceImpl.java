package in.clouthink.daas.fss.mongodb.spiImpl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import in.clouthink.daas.edm.Edms;
import in.clouthink.daas.edm.EventListener;
import in.clouthink.daas.fss.core.*;
import in.clouthink.daas.fss.domain.model.FileObject;
import in.clouthink.daas.fss.domain.model.MutableFileObject;
import in.clouthink.daas.fss.mongodb.impl.FileStorageImpl;
import in.clouthink.daas.fss.core.FileStorageService;
import in.clouthink.daas.fss.spi.MutableFileObjectService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dz
 */
public class FileStorageServiceImpl implements FileStorageService, EventListener<FileObject>, InitializingBean {

	private static final Log logger = LogFactory.getLog(FileStorageServiceImpl.class);

	@Autowired
	private MutableFileObjectService fileObjectService;

	@Override
	public Map<String,Object> buildExtraAttributes(FileObject fileObject) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("downloadUrl", String.format("/api/files/%s/download", fileObject.getId()));
		return result;
	}

	@Override
	public FileStorage store(InputStream inputStream, StoreFileRequest request) {
		FileObject fileObject = createMongodbFileObject(request);
		updateGridfsPart((MutableFileObject) fileObject);
		fileObject = doStore(inputStream, fileObject);
		fileObject = fileObjectService.save(fileObject);

		GridFSDBFile gridFSDBFile = gridFSService.getGridFS().findOne(fileObject.getStoredFilename());
		return new in.clouthink.daas.fss.mongodb.impl.FileStorageImpl(fileObject, gridFSDBFile);
	}

	@Override
	public FileStorage restore(String id, InputStream inputStream, StoreFileRequest request) {
		if (StringUtils.isEmpty(id)) {
			throw new StoreFileException("The file object id is required.");
		}
		FileObject fileObject = fileObjectService.findById(id);
		if (fileObject == null) {
			throw new StoreFileException("The file object of specified id is not found.");
		}

		fileObjectService.saveAsHistory(fileObject);

		fileObject = fileObjectService.merge(request, fileObject);
		updateGridfsPart((MutableFileObject) fileObject);
		fileObject = doStore(inputStream, fileObject);
		fileObject = fileObjectService.save(fileObject);

		GridFSDBFile gridFSDBFile = gridFSService.getGridFS().findOne(fileObject.getStoredFilename());
		return new in.clouthink.daas.fss.mongodb.impl.FileStorageImpl(fileObject, gridFSDBFile);
	}

	@Override
	public FileStorage findById(String id) {
		final FileObject fileObject = fileObjectService.findById(id);
		if (fileObject == null) {
			return null;
		}

		GridFSDBFile gridFSDBFile = gridFSService.getGridFS().findOne(fileObject.getStoredFilename());
		return new in.clouthink.daas.fss.mongodb.impl.FileStorageImpl(fileObject, gridFSDBFile);
	}

	@Override
	public FileStorage findByFilename(String filename) {
		final FileObject fileObject = fileObjectService.findByStoredFilename(filename);
		if (fileObject == null) {
			return null;
		}

		GridFSDBFile gridFSDBFile = gridFSService.getGridFS().findOne(fileObject.getStoredFilename());
		return new FileStorageImpl(fileObject, gridFSDBFile);
	}

	@Override
	public void onEvent(FileObject fileObject) {
		gridFSService.getGridFS().remove(fileObject.getStoredFilename());
	}

	private in.clouthink.daas.fss.mongodb.model.FileObject createMongodbFileObject(StoreFileRequest request) {
		//validate
		if (StringUtils.isEmpty(request.getOriginalFilename())) {
			throw new StoreFileException("The originalFilename is required.");
		}
		if (StringUtils.isEmpty(request.getUploadedBy())) {
			throw new StoreFileException("The uploadedBy is required.");
		}

		//create file object from request
		in.clouthink.daas.fss.mongodb.model.FileObject fileObject = in.clouthink.daas.fss.mongodb.model.FileObject
                .from(
				request);

		//pretty filename
		if (StringUtils.isEmpty(fileObject.getPrettyFilename())) {
			fileObject.setPrettyFilename(fileObject.getOriginalFilename());
		}

		//version
		fileObject.setVersion(1);

		//uploaded at
		Date uploadedAt = new Date();
		fileObject.setUploadedAt(uploadedAt);
		//

		return fileObject;
	}

	private void updateGridfsPart(MutableFileObject fileObject) {
		//gridfs filename

		//extra attributes
		fileObject.getAttributes().put("fss-provider", "gridfs");
	}

	private FileObject doStore(InputStream inputStream, FileObject fileObject) {



		GridFSInputFile gfsInputFile = gridFSService.getGridFS().createFile(inputStream);
		gfsInputFile.setFilename(fileObject.getStoredFilename());
		gfsInputFile.setContentType(fileObject.getContentType());
		if (fileObject.getAttributes() != null) {
			DBObject dbObject = new BasicDBObject(fileObject.getAttributes());
			gfsInputFile.setMetaData(dbObject);
		}
		gfsInputFile.save();
		return fileObject;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Edms.getEdm().register("in.clouthink.daas.fss#delete", this);
	}

}

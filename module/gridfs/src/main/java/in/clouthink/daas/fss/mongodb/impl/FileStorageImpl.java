package in.clouthink.daas.fss.mongodb.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import in.clouthink.daas.fss.core.StoreFileException;
import in.clouthink.daas.fss.core.StoreFileRequest;
import in.clouthink.daas.fss.core.StoreFileResponse;
import in.clouthink.daas.fss.core.FileStorage;
import in.clouthink.daas.fss.support.DefaultFileObject;
import in.clouthink.daas.fss.support.DefaultStoreFileResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * @author dz
 */
public class FileStorageImpl implements FileStorage, InitializingBean {

	private static final Log logger = LogFactory.getLog(FileStorageImpl.class);

	public static final String GRIDFS_COLLECTION_NAME = "FileObjects";

	private String collectionName = GRIDFS_COLLECTION_NAME;

	@Autowired
	private MongoDbFactory mongoDbFactory;

	private GridFS gridFS;

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		if (StringUtils.isEmpty(collectionName)) {
			throw new IllegalArgumentException("The collection name can't be null or empty");
		}
		this.collectionName = collectionName;
	}

	public GridFS getGridFS() {
		return gridFS;
	}


	@Override
	public String getName() {
		return "gridfs";
	}

	@Override
	public StoreFileResponse store(InputStream inputStream, StoreFileRequest request) throws StoreFileException {
		//generate the stored file name
		String finalFilename = UUID.randomUUID().toString().replace("-", "");
		String extName = in.clouthink.daas.fss.repackage.org.apache.commons.io.FilenameUtils.getExtension(fileObject.getOriginalFilename());
		if (!StringUtils.isEmpty(extName)) {
			finalFilename += "." + extName;
		}

		GridFSInputFile gfsInputFile = this.gridFS.createFile(inputStream);
		gfsInputFile.setFilename(finalFilename);
		gfsInputFile.setContentType(request.getContentType());
		if (request.getAttributes() != null) {
			DBObject dbObject = new BasicDBObject(request.getAttributes());
			gfsInputFile.setMetaData(dbObject);
		}
		gfsInputFile.save();

		DefaultFileObject fileObject = new DefaultFileObject();

		return new DefaultStoreFileResponse();
	}

	@Override
	public StoreFileResponse store(File file, StoreFileRequest request) throws StoreFileException {
		return null;
	}

	@Override
	public StoreFileResponse store(byte[] bytes, StoreFileRequest request) throws StoreFileException {
		return null;
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		DB db = mongoDbFactory.getDb();
		this.gridFS = new GridFS(db, collectionName);
	}

}

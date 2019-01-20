package in.clouthink.daas.fss.mongodb.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;
import in.clouthink.daas.fss.core.*;
import in.clouthink.daas.fss.support.DefaultStoreFileResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.UUID;

/**
 * @author dz
 */
public class FileStorageImpl implements FileStorage, InitializingBean {

    private static final Log logger = LogFactory.getLog(FileStorageImpl.class);

    public static final String PROVIDER_NAME = "gridfs";

    public static final String GRIDFS_COLLECTION_NAME = "FileObjects";

    @Autowired
    private MongoDbFactory mongoDbFactory;

    private GridFS gridFS;

    private String collectionName = GRIDFS_COLLECTION_NAME;

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
        return PROVIDER_NAME;
    }

    @Override
    public StoreFileResponse store(InputStream inputStream, StoreFileRequest request) throws StoreFileException {
        //generate the stored file name
        String filenameToStore = UUID.randomUUID().toString().replace("-", "");
        String extName = in.clouthink.daas.fss.repackage.org.apache.commons.io.FilenameUtils.getExtension(request.getOriginalFilename());
        if (!StringUtils.isEmpty(extName)) {
            filenameToStore += "." + extName;
        }

        GridFSInputFile gfsInputFile = this.gridFS.createFile(inputStream);
        gfsInputFile.setFilename(filenameToStore);
        gfsInputFile.setContentType(request.getContentType());
        if (request.getAttributes() != null) {
            DBObject dbObject = new BasicDBObject(request.getAttributes());
            gfsInputFile.setMetaData(dbObject);
        }
        gfsInputFile.save();

        DefaultStoredFileObject fileObject = DefaultStoredFileObject.from(request);
        fileObject.setStoredFilename(filenameToStore);
        fileObject.setProviderName(PROVIDER_NAME);
        fileObject.setImplementation(gfsInputFile);

        return new DefaultStoreFileResponse(PROVIDER_NAME, fileObject);
    }

    @Override
    public StoreFileResponse store(File file, StoreFileRequest request) throws StoreFileException {
        try {
            return store(new FileInputStream(file), request);
        } catch (FileNotFoundException e) {
            throw new StoreFileException(file.getName() + " not found.", e);
        }
    }

    @Override
    public StoreFileResponse store(byte[] bytes, StoreFileRequest request) throws StoreFileException {
        return store(new ByteArrayInputStream(bytes), request);
    }

    @Override
    public StoredFileObject findByStoredFilename(String filename) {
        return null;
    }

    @Override
    public StoredFileObject delete(String filename) {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        DB db = mongoDbFactory.getDb();
        this.gridFS = new GridFS(db, collectionName);
    }

}

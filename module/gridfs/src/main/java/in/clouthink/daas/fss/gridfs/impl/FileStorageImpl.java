package in.clouthink.daas.fss.gridfs.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import in.clouthink.daas.fss.core.*;
import in.clouthink.daas.fss.support.DefaultStoreFileResponse;
import in.clouthink.daas.fss.util.MetadataUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
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

    //auto created after the bean is initialized.
    private GridFS gridFS;

    private String collectionName = GRIDFS_COLLECTION_NAME;

    public String getCollectionName() {
        return this.collectionName;
    }

    public void setCollectionName(String collectionName) {
        if (StringUtils.isEmpty(collectionName)) {
            throw new IllegalArgumentException("The collection name can't be null or empty");
        }
        this.collectionName = collectionName;
    }

    public GridFS getGridFS() {
        return this.gridFS;
    }

    @Override
    public String getName() {
        return PROVIDER_NAME;
    }

    @Override
    public boolean isMetadataSupported() {
        return true;
    }

    @Override
    public boolean isImageSupported() {
        return false;
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
        Map<String, String> metadata = MetadataUtils.buildMetadata(request);
        DBObject dbObject = new BasicDBObject(metadata);
        gfsInputFile.setMetaData(dbObject);
        gfsInputFile.save();

        logger.debug(String.format("%s is stored", filenameToStore));

        GridFSDBFile gridFSDBFile = this.gridFS.findOne(filenameToStore);

        DefaultStoredFileObject fileObject = DefaultStoredFileObject.from(request);
        fileObject.setSize(gridFSDBFile.getChunkSize());
        fileObject.setUploadedAt(gridFSDBFile.getUploadDate());
        fileObject.setStoredFilename(filenameToStore);
        fileObject.setProviderName(PROVIDER_NAME);
        fileObject.setImplementation(gridFSDBFile);

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
        GridFSDBFile gridFSDBFile = this.gridFS.findOne(filename);

        if (gridFSDBFile == null) {
            return null;
        }

        DefaultStoredFileObject fileObject = new DefaultStoredFileObject();
        buildStoreFileObject(gridFSDBFile, fileObject);

        fileObject.setProviderName(PROVIDER_NAME);
        fileObject.setImplementation(gridFSDBFile);

        return fileObject;
    }

    @Override
    public StoredFileObject findByStoredFilename(String filename, String downloadUrl) {
        logger.warn(String.format("Caution: The download url[%s] will be skipped", downloadUrl));
        return findByStoredFilename(filename);
    }

    @Override
    public StoredFileObject delete(String filename) {
        GridFSDBFile gridFSDBFile = this.gridFS.findOne(filename);

        if (gridFSDBFile == null) {
            return null;
        }

        DefaultStoredFileObject fileObject = new DefaultStoredFileObject();
        buildStoreFileObject(gridFSDBFile, fileObject);

        fileObject.setProviderName(PROVIDER_NAME);
        fileObject.setImplementation(null);

        try {
            this.gridFS.remove(filename);
            logger.info(String.format("The gridfs file %s is deleted.", filename));
        } catch (Throwable e) {
            logger.error(String.format("Delete gridfs file %s failed.", filename), e);
        }

        return fileObject;
    }

    private void buildStoreFileObject(GridFSDBFile gridFSDBFile, DefaultStoredFileObject fileObject) {
        fileObject.setUploadedAt(gridFSDBFile.getUploadDate());
        fileObject.setSize(gridFSDBFile.getChunkSize());
        fileObject.setStoredFilename(gridFSDBFile.getFilename());

        DBObject dbObject = gridFSDBFile.getMetaData();

        if (dbObject == null) {
            return;
        }

        try {
            fileObject.setOriginalFilename((String) dbObject.get("fss-originalFilename"));
            fileObject.setPrettyFilename((String) dbObject.get("fss-prettyFilename"));
            fileObject.setContentType((String) dbObject.get("fss-contentType"));
            fileObject.setUploadedBy((String) dbObject.get("fss-uploadedBy"));
        } catch (Throwable e) {
            logger.error(e, e);
        }

        try {
            Map<String, String> attributes = new HashMap<>();
            dbObject.keySet().stream().filter(key -> key.startsWith("fss-attrs-")).forEach(key -> {
                String attributeName = key.substring("fss-attrs-".length());
                attributes.put(attributeName, (String) dbObject.get(key));
            });
            fileObject.setAttributes(attributes);
        } catch (Throwable e) {
            logger.error(e, e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        DB db = mongoDbFactory.getDb();
        this.gridFS = new GridFS(db, collectionName);
    }

}

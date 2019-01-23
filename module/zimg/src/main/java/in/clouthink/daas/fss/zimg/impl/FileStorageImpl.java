package in.clouthink.daas.fss.zimg.impl;

import in.clouthink.daas.fss.core.*;
import in.clouthink.daas.fss.support.DefaultStoreFileResponse;
import in.clouthink.daas.fss.zimg.client.ZimgResult;
import in.clouthink.daas.fss.zimg.exception.ZimgStoreException;
import in.clouthink.daas.fss.zimg.support.ZimgProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.io.*;
import java.util.Date;

/**
 * @author LiangBin & dz
 */
public class FileStorageImpl implements FileStorage, InitializingBean {

    private static final Log logger = LogFactory.getLog(FileStorageImpl.class);

    public static final String PROVIDER_NAME = "zimg";

    @Autowired
    private ZimgProperties zimgProperties;

    @Autowired
    private ZimgClient zimgClient;

    @Override
    public String getName() {
        return PROVIDER_NAME;
    }

    @Override
    public StoreFileResponse store(InputStream inputStream, StoreFileRequest request) throws StoreFileException {
        String contentType = request.getContentType();
        if (contentType.indexOf("/") > 0) {
            contentType = contentType.split("/")[1];
        }

        ZimgResult zimgResult = zimgClient.upload(inputStream,
                                                  contentType,
                                                  request.getSize(),
                                                  zimgProperties.getUploadEndpoint());

        if (!zimgResult.isRet()) {
            throw new StoreFileException(zimgResult.getError().getMessage());
        }

        logger.debug(String.format("%s is stored", zimgResult.getInfo().getMd5()));

        DefaultStoredFileObject fileObject = DefaultStoredFileObject.from(request);

        fileObject.getAttributes().put("zimg-md5", zimgResult.getInfo().getMd5());

        fileObject.setUploadedAt(new Date());

        fileObject.setStoredFilename(zimgResult.getInfo().getMd5());
        fileObject.setProviderName(PROVIDER_NAME);
        //TODO
//        fileObject.setImplementation();

        return new DefaultStoreFileResponse(PROVIDER_NAME, fileObject);
    }

    @Override
    public StoreFileResponse store(File file, StoreFileRequest request) throws StoreFileException {
        try {
            return store(new FileInputStream(file), request);
        } catch (FileNotFoundException e) {
            throw new ZimgStoreException(file.getName() + " not found.", e);
        }
    }

    @Override
    public StoreFileResponse store(byte[] bytes, StoreFileRequest request) throws StoreFileException {
        return store(new ByteArrayInputStream(bytes), request);
    }

    @Override
    public StoredFileObject findByStoredFilename(String filename) {
        DefaultStoredFileObject fileObject = new DefaultStoredFileObject();

        fileObject.setStoredFilename(filename);
        fileObject.setProviderName(PROVIDER_NAME);
        //TODO
//        fileObject.setImplementation();

        return fileObject;
    }

    @Override
    public StoredFileObject delete(String filename) {
        DefaultStoredFileObject fileObject = new DefaultStoredFileObject();

        fileObject.setStoredFilename(filename);
        fileObject.setProviderName(PROVIDER_NAME);
        fileObject.setImplementation(null);

        try {
            zimgClient.delete(filename, zimgProperties.getDownloadEndpoint());
            logger.info(String.format("The file [%s] is deleted.", filename));
        } catch (Throwable e) {
            logger.error(String.format("Delete the file [%s] failed.", filename), e);
        }

        return fileObject;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(zimgProperties);
        Assert.notNull(zimgClient);
    }

}

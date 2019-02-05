package in.clouthink.daas.fss.webdav.impl;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import in.clouthink.daas.fss.core.*;
import in.clouthink.daas.fss.support.DefaultStoreFileResponse;
import in.clouthink.daas.fss.util.MetadataUtils;
import in.clouthink.daas.fss.webdav.exception.WebDavStoreException;
import in.clouthink.daas.fss.webdav.support.WebDavProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Date;

/**
 * @author dz
 * @since 3
 */
public class FileStorageImpl implements FileStorage, InitializingBean {

    private static final Log logger = LogFactory.getLog(FileStorageImpl.class);

    public static final String PROVIDER_NAME = "webdav";

    @Autowired
    private WebDavProperties webDavProperties;

    private Sardine sardine;

    public WebDavProperties getWebDavProperties() {
        return webDavProperties;
    }

    public Sardine getSardine() {
        return sardine;
    }

    @Override
    public String getName() {
        return PROVIDER_NAME;
    }

    @Override
    public boolean isMetadataSupported() {
        return false;
    }

    @Override
    public StoreFileResponse store(InputStream inputStream, StoreFileRequest request) throws StoreFileException {
        String filenameToSave = MetadataUtils.generateKey(request);

        String uploadEndpoint = webDavProperties.getUploadEndpoint();
        if (!uploadEndpoint.endsWith("/")) {
            uploadEndpoint = uploadEndpoint.concat("/");
        }

        String remoteFilename = uploadEndpoint.concat(filenameToSave);

        try {
            sardine.put(remoteFilename, inputStream, request.getContentType());

            logger.debug(String.format("%s is stored", filenameToSave));

            DefaultStoredFileObject fileObject = DefaultStoredFileObject.from(request);

            fileObject.getAttributes().put("webdav-filename", remoteFilename);

            fileObject.setUploadedAt(new Date());

            fileObject.setStoredFilename(filenameToSave);
            fileObject.setProviderName(PROVIDER_NAME);
            fileObject.setImplementation(new WebDavFile(remoteFilename,
                                                        sardine));

            return new DefaultStoreFileResponse(PROVIDER_NAME, fileObject);
        } catch (Throwable e) {
            throw new WebDavStoreException(String.format("Fail to upload file %s", filenameToSave), e);
        }
    }

    @Override
    public StoreFileResponse store(File file, StoreFileRequest request) throws StoreFileException {
        try {
            return store(new FileInputStream(file), request);
        } catch (FileNotFoundException e) {
            throw new WebDavStoreException(file.getName() + " not found.", e);
        }
    }

    @Override
    public StoreFileResponse store(byte[] bytes, StoreFileRequest request) throws StoreFileException {
        return store(new ByteArrayInputStream(bytes), request);
    }

    @Override
    public StoredFileObject findByStoredFilename(String filename) {
        if (StringUtils.isEmpty(filename)) {
            return null;
        }

        if (filename.indexOf("?") > 0) {
            filename = filename.substring(0, filename.indexOf("?"));
        }

        String uploadEndpoint = webDavProperties.getUploadEndpoint();
        if (!uploadEndpoint.endsWith("/")) {
            uploadEndpoint = uploadEndpoint.concat("/");
        }

        String remoteFilename = uploadEndpoint.concat(filename);

        try {
            if (!sardine.exists(remoteFilename)) {
                return null;
            }
        } catch (IOException e) {
            logger.error(String.format("Fail to check file %s existing", filename));
            return null;
        }

        DefaultStoredFileObject fileObject = new DefaultStoredFileObject();

        fileObject.setStoredFilename(filename);
        fileObject.setProviderName(PROVIDER_NAME);

        fileObject.setImplementation(new WebDavFile(remoteFilename,
                                                    sardine));

        return fileObject;
    }

    @Override
    public StoredFileObject delete(String filename) {
        if (StringUtils.isEmpty(filename)) {
            return null;
        }

        if (filename.indexOf("?") > 0) {
            filename = filename.substring(0, filename.indexOf("?"));
        }

        String uploadEndpoint = webDavProperties.getUploadEndpoint();
        if (!uploadEndpoint.endsWith("/")) {
            uploadEndpoint = uploadEndpoint.concat("/");
        }

        String remoteFilename = uploadEndpoint.concat(filename);

        try {
            if (!sardine.exists(remoteFilename)) {
                return null;
            }
        } catch (IOException e) {
            logger.error(String.format("Fail to check file %s existing", filename));
            return null;
        }

        DefaultStoredFileObject fileObject = new DefaultStoredFileObject();

        fileObject.setStoredFilename(filename);
        fileObject.setProviderName(PROVIDER_NAME);
        fileObject.setImplementation(null);

        try {
            sardine.delete(remoteFilename);
            logger.info(String.format("The file [%s] is deleted", filename));
        } catch (Throwable e) {
            logger.error(String.format("Fail to delete the file [%s]", filename), e);
        }

        return fileObject;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(webDavProperties);
        this.sardine = SardineFactory.begin(webDavProperties.getUsername(), webDavProperties.getPassword());
    }

}

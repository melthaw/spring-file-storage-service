package in.clouthink.daas.fss.webdav.impl;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import com.github.sardine.impl.SardineException;
import com.github.sardine.impl.SardineImpl;
import com.github.sardine.util.SardineUtil;
import in.clouthink.daas.fss.core.*;
import in.clouthink.daas.fss.support.DefaultStoreFileResponse;
import in.clouthink.daas.fss.util.MetadataUtils;
import in.clouthink.daas.fss.webdav.exception.WebDavStoreException;
import in.clouthink.daas.fss.webdav.support.WebDavProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.xml.namespace.QName;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return true;
    }

    @Override
    public StoreFileResponse store(InputStream inputStream, StoreFileRequest request) throws StoreFileException {
        String filenameToSave = MetadataUtils.generateFilename(request);

        //prepare base endpoint
        String uploadEndpoint = webDavProperties.getEndpoint();
        if (!uploadEndpoint.endsWith("/")) {
            uploadEndpoint = uploadEndpoint.concat("/");
        }

        //try create dir before store file
        try {
            String directory = createDirectory(uploadEndpoint);
            //update filename to save
            filenameToSave = directory.concat("/").concat(filenameToSave);
        } catch (Throwable e) {
            logger.error(String.format("Fail to create path %s", uploadEndpoint), e);
        }

        String remoteFilename = uploadEndpoint.concat(filenameToSave);

        try {
            sardine.put(remoteFilename, inputStream, request.getContentType());
            logger.debug(String.format("%s is stored to %s", request.getOriginalFilename(), remoteFilename));
        } catch (Throwable e) {
            throw new WebDavStoreException(String.format("Fail to upload file %s", remoteFilename), e);
        }

        //try to store metadata
        Map<String, String> metadata = MetadataUtils.buildMetadata(request);

        try {
            sardine.patch(remoteFilename, SardineUtil.toQName(metadata));
            logger.debug(String.format("Metadata for %s is stored", remoteFilename));
        } catch (Throwable e) {
            //only logging the ex if fail to store the metadata
            logger.error("Fail to set webdav metadata", e);
        }

        DefaultStoredFileObject fileObject = DefaultStoredFileObject.from(request);

        fileObject.setUploadedAt(new Date());
        fileObject.setStoredFilename(filenameToSave);
        fileObject.setFileUrl(remoteFilename);
        fileObject.setProviderName(PROVIDER_NAME);
        fileObject.setImplementation(new WebDavFile(remoteFilename, sardine));

        return new DefaultStoreFileResponse(PROVIDER_NAME, fileObject);
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

        String uploadEndpoint = webDavProperties.getEndpoint();
        if (!uploadEndpoint.endsWith("/")) {
            uploadEndpoint = uploadEndpoint.concat("/");
        }

        String remoteFilename = uploadEndpoint.concat(filename);

        try {
            if (!sardine.exists(remoteFilename)) {
                return null;
            }
        } catch (IOException e) {
            logger.error(String.format("Fail to get file %s", filename));
            return null;
        }

        DefaultStoredFileObject fileObject = new DefaultStoredFileObject();

        try {
            List<DavResource> davResources = sardine.list(remoteFilename);
            if (davResources != null && davResources.size() == 1) {
                DavResource davResource = davResources.get(0);
                buildStoreFileObject(davResource, fileObject);
            }
        } catch (IOException e) {
            logger.error(String.format("Fail to resolve metadata of %s", remoteFilename), e);
        }

        fileObject.setStoredFilename(filename);
        fileObject.setProviderName(PROVIDER_NAME);
        fileObject.setImplementation(new WebDavFile(remoteFilename, sardine));

        return fileObject;
    }

    @Override
    public StoredFileObject findByStoredFilename(String filename, String downloadUrl) {
        logger.warn(String.format("Caution: The download url[%s] will be skipped", downloadUrl));
        return findByStoredFilename(filename);
    }

    @Override
    public StoredFileObject delete(String filename) {
        if (StringUtils.isEmpty(filename)) {
            return null;
        }

        if (filename.indexOf("?") > 0) {
            filename = filename.substring(0, filename.indexOf("?"));
        }

        String uploadEndpoint = webDavProperties.getEndpoint();
        if (!uploadEndpoint.endsWith("/")) {
            uploadEndpoint = uploadEndpoint.concat("/");
        }

        String remoteFilename = uploadEndpoint.concat(filename);

        try {
            if (!sardine.exists(remoteFilename)) {
                return null;
            }
        } catch (IOException e) {
            logger.error(String.format("Fail to delete file %s", filename));
            return null;
        }

        DefaultStoredFileObject fileObject = new DefaultStoredFileObject();

        try {
            List<DavResource> davResources = sardine.list(remoteFilename);
            if (davResources != null && davResources.size() == 1) {
                DavResource davResource = davResources.get(0);
                buildStoreFileObject(davResource, fileObject);
            }
        } catch (IOException e) {
            logger.error(String.format("Fail to resolve metadata of %s", remoteFilename), e);
        }

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

    private void buildStoreFileObject(DavResource davResource, DefaultStoredFileObject fileObject) {
        Map<String, String> userMetadata = davResource.getCustomProps();

        if (userMetadata == null) {
            return;
        }

        try {
            fileObject.setOriginalFilename(userMetadata.get("fss-originalFilename"));
            fileObject.setPrettyFilename(userMetadata.get("fss-prettyFilename"));
            fileObject.setContentType(userMetadata.get("fss-contentType"));
            fileObject.setUploadedBy(userMetadata.get("fss-uploadedBy"));

            String uploadedAt = userMetadata.get("fss-uploadedAt");
            fileObject.setUploadedAt(uploadedAt != null ? new Date(Long.parseLong(uploadedAt)) : null);

            String size = userMetadata.get("fss-size");
            fileObject.setSize(size != null ? Long.parseLong(size) : -1);
        } catch (Throwable e) {
            logger.error(e, e);
        }

        try {
            Map<String, String> attributes = new HashMap<>();
            userMetadata.keySet().stream().filter(key -> key.startsWith("fss-attrs-")).forEach(key -> {
                String attributeName = key.substring("fss-attrs-".length());
                attributes.put(attributeName, userMetadata.get(key));
            });
            fileObject.setAttributes(attributes);
        } catch (Throwable e) {
            logger.error(e, e);
        }
    }


    private String createDirectory(String uploadEndpoint) throws IOException {
        Date now = new Date();

        if (!uploadEndpoint.endsWith("/")) {
            uploadEndpoint = uploadEndpoint.concat("/");
        }

        //try create path of year
        uploadEndpoint = uploadEndpoint.concat(new SimpleDateFormat("yyyy").format(now));

        try {
            if (!sardine.exists(uploadEndpoint)) {
                sardine.createDirectory(uploadEndpoint);
            }
        } catch (Throwable e) {
            throw e;
        }

        //try create path of month
        uploadEndpoint = uploadEndpoint.concat("/").concat(new SimpleDateFormat("MM").format(now));

        try {
            if (!sardine.exists(uploadEndpoint)) {
                sardine.createDirectory(uploadEndpoint);
            }
        } catch (Throwable e) {
            throw e;
        }

        //try create path of day
        uploadEndpoint = uploadEndpoint.concat("/").concat(new SimpleDateFormat("dd").format(now));

        try {
            if (!sardine.exists(uploadEndpoint)) {
                sardine.createDirectory(uploadEndpoint);
            }
        } catch (Throwable e) {
            throw e;
        }

        return new SimpleDateFormat("yyyy/MM/dd").format(now);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.webDavProperties);
        if (this.webDavProperties.isSslEnabled()) {
            //FIXME
            final SSLSocketFactory socketFactory = new SSLSocketFactory(new TrustSelfSignedStrategy());
            this.sardine =  new SardineImpl() {
                @Override
                protected SSLSocketFactory createDefaultSecureSocketFactory() {
                    return socketFactory;
                }
            };
        }
        else {
            this.sardine = SardineFactory.begin(webDavProperties.getUsername(), webDavProperties.getPassword());
        }
    }

}

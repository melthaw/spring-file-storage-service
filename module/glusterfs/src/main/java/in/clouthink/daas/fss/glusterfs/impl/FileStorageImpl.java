package in.clouthink.daas.fss.glusterfs.impl;

import in.clouthink.daas.fss.core.*;
import in.clouthink.daas.fss.glusterfs.exception.GlusterfsStoreException;
import in.clouthink.daas.fss.glusterfs.support.GlusterfsProperties;
import in.clouthink.daas.fss.support.DefaultStoreFileResponse;
import in.clouthink.daas.fss.util.MetadataUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.spi.FileSystemProvider;
import java.util.Date;
import java.util.Set;

/**
 * @author dz
 */
public class FileStorageImpl implements FileStorage, InitializingBean {

    private static final Log logger = LogFactory.getLog(FileStorageImpl.class);

    public static final String PROVIDER_NAME = "glusterfs";

    public static final String PROTOCOL = "gluster";

    @Autowired
    private GlusterfsProperties glusterfsProperties;

    public GlusterfsProperties getGlusterfsProperties() {
        return glusterfsProperties;
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
    public boolean isImageSupported() {
        return false;
    }

    @Override
    public StoreFileResponse store(InputStream inputStream, StoreFileRequest request) throws StoreFileException {
        Set<PosixFilePermission> posixFilePermissions = PosixFilePermissions.fromString("rw-rw-rw-");
        FileAttribute<Set<PosixFilePermission>> posixFileAttrs = PosixFilePermissions.asFileAttribute(
                posixFilePermissions);

        String fileToSave = MetadataUtils.generateFilename(request);
        String saveUri = "gluster://" +
                glusterfsProperties.getServer() + ":" +
                glusterfsProperties.getVolume() + "/" +
                fileToSave;
        try {
            Path savePath = Paths.get(new URI(saveUri));
            savePath = Files.createFile(savePath, posixFileAttrs);

            logger.debug(String.format("% is created", fileToSave));

            byte[] buffer = new byte[glusterfsProperties.getBufferSize()];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                Files.write(savePath, buffer, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
            }

            logger.debug(String.format("% is stored", fileToSave));

            DefaultStoredFileObject fileObject = DefaultStoredFileObject.from(request);

            fileObject.setUploadedAt(new Date());
            fileObject.setStoredFilename(fileToSave);
            fileObject.setProviderName(PROVIDER_NAME);
            fileObject.setImplementation(new GlusterFile(savePath));

            return new DefaultStoreFileResponse(PROVIDER_NAME, fileObject);
        } catch (URISyntaxException e) {
            throw new GlusterfsStoreException(String.format("Invalid file uri %s", fileToSave), e);
        } catch (IOException e) {
            throw new GlusterfsStoreException(String.format("Fail to save %s", fileToSave), e);
        }
    }

    @Override
    public StoreFileResponse store(File file, StoreFileRequest request) throws StoreFileException {
        try {
            return store(new FileInputStream(file), request);
        } catch (FileNotFoundException e) {
            throw new GlusterfsStoreException(file.getName() + " not found.", e);
        }
    }

    @Override
    public StoreFileResponse store(byte[] bytes, StoreFileRequest request) throws StoreFileException {
        return store(new ByteArrayInputStream(bytes), request);
    }

    @Override
    public StoredFileObject findByStoredFilename(String filename) {
        String fileUri = getGlusterFilename(filename);

        Path filePath = null;
        try {
            filePath = Paths.get(new URI(fileUri));
        } catch (URISyntaxException e) {
            logger.warn(String.format("Invalid filename %s", filename));
            return null;
        }

        if (Files.exists(filePath)) {
            return null;
        }

        DefaultStoredFileObject fileObject = new DefaultStoredFileObject();

        fileObject.setStoredFilename(filename);
        fileObject.setProviderName(PROVIDER_NAME);
        fileObject.setImplementation(new GlusterFile(filePath));

        return fileObject;
    }

    @Override
    public StoredFileObject delete(String filename) {
        String fileUri = getGlusterFilename(filename);
        Path filePath = null;
        try {
            filePath = Paths.get(new URI(fileUri));
        } catch (URISyntaxException e) {
            logger.warn(String.format("Invalid filename %s", filename));
            return null;
        }

        if (Files.exists(filePath)) {
            return null;
        }

        DefaultStoredFileObject fileObject = new DefaultStoredFileObject();

        fileObject.setStoredFilename(filename);
        fileObject.setProviderName(PROVIDER_NAME);
        fileObject.setImplementation(null);

        try {
            Files.deleteIfExists(filePath);
            logger.info(String.format("The file [%s] is deleted", filename));
        } catch (IOException e) {
            logger.error(String.format("Fail to delete the file [%s]", filename), e);
        }

        return fileObject;
    }

    private String getGlusterFilename(String filename) {
        if (filename.indexOf("?") > 0) {
            filename = filename.substring(0, filename.indexOf("?"));
        }

        return "gluster://" +
                glusterfsProperties.getServer() + ":" +
                glusterfsProperties.getVolume() + "/" +
                filename;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(glusterfsProperties);
        boolean glusterSupported = false;
        for (FileSystemProvider fsp : FileSystemProvider.installedProviders()) {
            if (fsp.getScheme().equals(PROTOCOL)) {
                glusterSupported = true;
                break;
            }
        }

        if (!glusterSupported) {
            throw new GlusterfsStoreException(
                    "Gluster file system is not supported, please make sure the glusterfs is enabled. ");
        }
    }

}

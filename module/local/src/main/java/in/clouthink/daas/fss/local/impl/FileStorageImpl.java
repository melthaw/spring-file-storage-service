package in.clouthink.daas.fss.local.impl;

import in.clouthink.daas.fss.core.*;
import in.clouthink.daas.fss.local.exception.LocalFileStoreException;
import in.clouthink.daas.fss.local.support.LocalFileProperties;
import in.clouthink.daas.fss.support.DefaultStoreFileResponse;
import in.clouthink.daas.fss.util.IOUtils;
import in.clouthink.daas.fss.util.MetadataUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Date;

/**
 * The file storage impl for local file system
 *
 * @author dz
 */
public class FileStorageImpl implements FileStorage, InitializingBean {

    private static final Log logger = LogFactory.getLog(FileStorageImpl.class);

    public static final String PROVIDER_NAME = "local";

    @Autowired
    private LocalFileProperties fsProperties;

    public LocalFileProperties getFsProperties() {
        return fsProperties;
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
        String storedFileName = MetadataUtils.generateFilename(request, true);

        String userSuppliedPath = request.getAttributes().get("fss-prefix-path");
        if (!StringUtils.isEmpty(userSuppliedPath)) {
            storedFileName = MetadataUtils.generateFilename(request, userSuppliedPath);
        }

        // 上传文件
        try {
            Path path = FileSystems.getDefault().getPath(fsProperties.getStorePath(), storedFileName);
            File file = path.toFile();
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            OutputStream outputStream = Files.newOutputStream(path, StandardOpenOption.CREATE);

            IOUtils.copy(inputStream, outputStream);

            logger.debug(String.format("The uploading %s is stored to [path=%s,filename=%s]",
                                       request.getOriginalFilename(),
                                       fsProperties.getStorePath(),
                                       storedFileName));

            DefaultStoredFileObject fileObject = DefaultStoredFileObject.from(request);

            fileObject.setStoredFilename(storedFileName);
            fileObject.setFileUrl(storedFileName);
            fileObject.setUploadedAt(new Date());
            fileObject.setProviderName(PROVIDER_NAME);

            try {
                fileObject.setSize(Files.size(path));
            } catch (Throwable e) {
                //try to fill the size from stored file , ignore if get to failure.
            }
            fileObject.setImplementation(path);

            return new DefaultStoreFileResponse(PROVIDER_NAME, fileObject);
        } catch (LocalFileStoreException e) {
            throw e;
        } catch (Throwable e) {
            throw new LocalFileStoreException(String.format("Fail to upload file %s", request.getOriginalFilename()),
                                              e);
        }
    }

    @Override
    public StoreFileResponse store(File file, StoreFileRequest request) throws StoreFileException {
        try {
            return store(new FileInputStream(file), request);
        } catch (FileNotFoundException e) {
            throw new LocalFileStoreException(String.format("%s not existed.", file.getName()), e);
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

        try {
            Path path = FileSystems.getDefault().getPath(fsProperties.getStorePath(), filename);
            if (!path.toFile().exists()) {
                logger.warn(String.format("File [%s] not found.", filename));
                return null;
            }

            DefaultStoredFileObject fileObject = new DefaultStoredFileObject();

            fileObject.setStoredFilename(filename);
            fileObject.setFileUrl(filename);
            fileObject.setProviderName(PROVIDER_NAME);
            fileObject.setImplementation(path);

            return fileObject;
        } catch (Throwable e) {
            logger.error(String.format("Fail to get the file [%s]", filename), e);
        }
        return null;
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

        try {
            Path path = FileSystems.getDefault().getPath(fsProperties.getStorePath(), filename);
            if (!path.toFile().exists()) {
                logger.warn(String.format("File [%s] not found.", filename));
                return null;
            }

            DefaultStoredFileObject fileObject = new DefaultStoredFileObject();

            fileObject.setStoredFilename(filename);
            fileObject.setFileUrl(filename);
            fileObject.setProviderName(PROVIDER_NAME);
            fileObject.setImplementation(null);

            Files.delete(path);
            logger.info(String.format("The file[path=%s , filename=%s] is deleted.",
                                      fsProperties.getStorePath(),
                                      filename));

            return fileObject;
        } catch (Throwable e) {
            logger.error(String.format("Fail to delete the file [%s]", filename), e);
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(this.fsProperties);
    }

}

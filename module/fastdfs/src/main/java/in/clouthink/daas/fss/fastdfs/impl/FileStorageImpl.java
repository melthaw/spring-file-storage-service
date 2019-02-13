package in.clouthink.daas.fss.fastdfs.impl;

import in.clouthink.daas.fss.core.*;
import in.clouthink.daas.fss.fastdfs.exception.FastdfsStoreException;
import in.clouthink.daas.fss.fastdfs.support.FastdfsProperties;
import in.clouthink.daas.fss.support.DefaultStoreFileResponse;
import in.clouthink.daas.fss.util.IOUtils;
import in.clouthink.daas.fss.util.MetadataUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The file storage impl for fastdfs ( using fastdfs-client-java 1.27-RELEASE ).
 *
 * @author dz
 */
public class FileStorageImpl implements FileStorage, InitializingBean, DisposableBean {

    private static final Log logger = LogFactory.getLog(FileStorageImpl.class);

    public static final String PROVIDER_NAME = "fastdfs";

    @Autowired
    private FastdfsProperties fastdfsProperties;

    private TrackerClient trackerClient;

    private TrackerServer trackerServer;

    private StorageServer storageServer;

    public FastdfsProperties getFastdfsProperties() {
        return fastdfsProperties;
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

        String originalFilename = request.getOriginalFilename();
        String fileExtName = in.clouthink.daas.fss.repackage.org.apache.commons.io.
                FilenameUtils.getExtension(originalFilename);

        // Metadata is supported by fastdfs, but something wrong with the fastdfs java client.
        Map<String, String> metadata = MetadataUtils.buildMetadata(request, true);

        NameValuePair[] metadataOfNameValuePair = metadata.entrySet()
                                                          .stream()
                                                          .map(entry -> new NameValuePair(entry.getKey(),
                                                                                          entry.getValue()))
                                                          .collect(Collectors.toList())
                                                          .toArray(new NameValuePair[]{});
        // 上传文件
        try {
            StorageClient1 storageClient = new StorageClient1(trackerServer, storageServer);
            String[] result = storageClient.uploadFile(IOUtils.copyToByteArray(inputStream),
                                                       fileExtName,
                                                       metadataOfNameValuePair);
            //null);

            if (result == null || result.length != 2) {
                throw new FastdfsStoreException(String.format("Fail to upload file %s , unknown reason.",
                                                              originalFilename));
            }

            String group_name = result[0];
            String remote_filename = result[1];

            logger.debug(String.format("%s is stored to group %s with name %s",
                                       originalFilename,
                                       group_name,
                                       remote_filename));

            DefaultStoredFileObject fileObject = DefaultStoredFileObject.from(request);

            fileObject.setStoredFilename(group_name + ":" + remote_filename);
            fileObject.setFileUrl(remote_filename);
            fileObject.setUploadedAt(new Date());
            fileObject.setProviderName(PROVIDER_NAME);
            fileObject.setImplementation(new FastFile(group_name, remote_filename, storageClient));

            return new DefaultStoreFileResponse(PROVIDER_NAME, fileObject);
        } catch (FastdfsStoreException e) {
            throw e;
        } catch (Throwable e) {
            throw new FastdfsStoreException(String.format("Fail to upload file %s", originalFilename), e);
        }
    }

    @Override
    public StoreFileResponse store(File file, StoreFileRequest request) throws StoreFileException {
        try {
            return store(new FileInputStream(file), request);
        } catch (FileNotFoundException e) {
            throw new FastdfsStoreException(String.format("%s not existed.", file.getName()), e);
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

        if (filename.indexOf(":") <= 0) {
            throw new FastdfsStoreException(String.format("Invalid filename %s", filename));
        }

        String group_name = filename.split(":")[0];
        String remote_filename = filename.split(":")[1];

        try {
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            FileInfo fileInfo = storageClient.getFileInfo(group_name, remote_filename);
            NameValuePair[] metadata = storageClient.getMetadata(group_name, remote_filename);

            if (fileInfo == null) {
                logger.warn(String.format("File [%s] not found.", filename));
                return null;
            }

            DefaultStoredFileObject fileObject = new DefaultStoredFileObject();

            buildStoreFileObject(metadata, fileObject);
            fileObject.setStoredFilename(filename);
            fileObject.setFileUrl(remote_filename);
            fileObject.setProviderName(PROVIDER_NAME);
            fileObject.setImplementation(new FastFile(group_name, remote_filename, storageClient));

            return fileObject;
        } catch (Throwable e) {
            logger.error(String.format("Fail to getG the file [%s]", filename), e);
        }
        return null;
    }

    @Override
    public StoredFileObject delete(String filename) {
        if (StringUtils.isEmpty(filename)) {
            return null;
        }

        if (filename.indexOf("?") > 0) {
            filename = filename.substring(0, filename.indexOf("?"));
        }

        String group_name = filename.split(":")[0];
        String remote_filename = filename.split(":")[1];
        try {
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            FileInfo fileInfo = storageClient.getFileInfo(group_name, remote_filename);
            NameValuePair[] metadata = storageClient.getMetadata(group_name, remote_filename);

            if (fileInfo == null) {
                logger.warn(String.format("File [%s] not found.", filename));
                return null;
            }

            DefaultStoredFileObject fileObject = new DefaultStoredFileObject();

            buildStoreFileObject(metadata, fileObject);
            fileObject.setStoredFilename(filename);
            fileObject.setFileUrl(remote_filename);
            fileObject.setProviderName(PROVIDER_NAME);
            fileObject.setImplementation(null);

            storageClient.deleteFile(group_name, remote_filename);
            logger.info(String.format("The file [%s] is deleted.", filename));

            return fileObject;
        } catch (Throwable e) {
            logger.error(String.format("Fail to delete the file [%s]", filename), e);
        }
        return null;
    }

    /**
     * There are some issues to upload metadata with fastdfs java client,  so we can't convert it back to file object.
     *
     * @param metadata
     * @param fileObject
     */
    private void buildStoreFileObject(NameValuePair[] metadata, DefaultStoredFileObject fileObject) {
        if (metadata == null || metadata.length == 0) {
            return;
        }

        Map<String, String> attributes = new HashMap<>();

        for (NameValuePair item : metadata) {
            try {
                if ("fss-originalFilename".equals(item.getName())) {
                    fileObject.setOriginalFilename(item.getValue());
                }
                else if ("fss-prettyFilename".equals(item.getName())) {
                    fileObject.setPrettyFilename(item.getValue());
                }
                else if ("fss-contentType".equals(item.getName())) {
                    fileObject.setContentType(item.getValue());
                }
                else if ("fss-uploadedBy".equals(item.getName())) {
                    fileObject.setUploadedBy(item.getValue());
                }
                else if ("fss-uploadedAt".equals(item.getName())) {
                    fileObject.setUploadedAt(
                            item.getValue() != null ? new Date(Long.parseLong(item.getValue())) : null);
                }
                else if ("fss-size".equals(item.getName())) {
                    fileObject.setSize(item.getValue() != null ? Long.parseLong(item.getValue()) : -1);
                }
                else if (item.getName().startsWith("fss-attrs-")) {
                    String attributeName = item.getName().substring("fss-attrs-".length());
                    attributes.put(attributeName, item.getValue());
                }
            } catch (Throwable e) {
                logger.error(e, e);
            }
        }

        fileObject.setAttributes(attributes);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.fastdfsProperties);

        try {
            ClientGlobal.setAntiStealToken(fastdfsProperties.isHttpAntiStealToken());
            ClientGlobal.setCharset(fastdfsProperties.getCharset());
            ClientGlobal.setConnectTimeout(fastdfsProperties.getConnectTimeoutInseconds());
            ClientGlobal.setNetworkTimeout(fastdfsProperties.getNetworkTimeoutInSeconds());
            ClientGlobal.setSecretKey(fastdfsProperties.getHttpSecretKey());

            TrackerGroup trackerGroup = new TrackerGroup(fastdfsProperties.getTrackerServers().stream().map(server -> {
                try {
                    return new InetSocketAddress(server.split(":")[0], Integer.parseInt(server.split(":")[1]));
                } catch (Throwable e) {
                    logger.error("Unresolvable tracker server " + server, e);
                }
                return null;
            }).filter(address -> address != null).collect(Collectors.toList()).toArray(new InetSocketAddress[]{}));
            ClientGlobal.setTrackerGroup(trackerGroup);
            ClientGlobal.setTrackerHttpPort(fastdfsProperties.getHttpTrackerHttpPort());

            this.trackerClient = new TrackerClient();
            this.trackerServer = trackerClient.getConnection();
            Assert.notNull(this.trackerServer);
            this.storageServer = trackerClient.getStoreStorage(this.trackerServer);
            Assert.notNull(this.storageServer);
        } catch (Throwable e) {
            throw new FastdfsStoreException("Fail to initialize FastDFS client.", e);
        }
    }

    @Override
    public void destroy() {
        try {
            this.trackerServer.close();
        } catch (Throwable e) {
        }
        try {
            this.storageServer.close();
        } catch (Throwable e) {
        }
    }
}

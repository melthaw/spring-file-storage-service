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
 * The file storage impl for fastdfs.
 *
 * @author dz
 */
public class FileStorageImpl implements FileStorage, InitializingBean, DisposableBean {

    private static final Log logger = LogFactory.getLog(FileStorageImpl.class);

    public static final String PROVIDER_NAME = "fastdfs";

    @Autowired
    private FastdfsProperties fastdfsProperties;

    private TrackerServer trackerServer;

    private StorageServer storageServer;

    private StorageClient client;

    public void setFastdfsProperties(FastdfsProperties fastdfsProperties) {
        this.fastdfsProperties = fastdfsProperties;
    }

    @Override
    public String getName() {
        return PROVIDER_NAME;
    }

    @Override
    public StoreFileResponse store(InputStream inputStream, StoreFileRequest request) throws StoreFileException {
        String originalFilename = request.getOriginalFilename();
        String fileExtName = in.clouthink.daas.fss.repackage.org.apache.commons.io.
                FilenameUtils.getExtension(originalFilename);

        Map<String, String> metadata = MetadataUtils.buildMetadata(request);

        // 上传文件
        try {
            String[] result = client.upload_file(IOUtils.copyToByteArray(inputStream),
                                                 fileExtName,
                                                 metadata.entrySet()
                                                         .stream()
                                                         .map(entry -> {
                                                             NameValuePair pair = new NameValuePair();
                                                             pair.setName(entry.getKey());
                                                             pair.setValue(entry.getValue());
                                                             return pair;
                                                         })
                                                         .collect(Collectors.toList())
                                                         .toArray(new NameValuePair[]{}));

            String group_name = result[0];
            String remote_filename = result[1];

            logger.debug(String.format("%s is stored to group %s with name %s",
                                       originalFilename,
                                       group_name,
                                       remote_filename));

            DefaultStoredFileObject fileObject = DefaultStoredFileObject.from(request);
            fileObject.getAttributes().put("fastdfs-group", group_name);

            fileObject.setUploadedAt(new Date());
            fileObject.setStoredFilename(remote_filename);
            fileObject.setProviderName(PROVIDER_NAME);
            fileObject.setImplementation(new FastFile(group_name,
                                                      remote_filename,
                                                      client));

            return new DefaultStoreFileResponse(PROVIDER_NAME, fileObject);
        } catch (Throwable e) {
            logger.warn(String.format("Fail to upload file %s", originalFilename));
            throw new FastdfsStoreException(String.format("Fail to upload file %s", originalFilename), e);
        }
    }

    @Override
    public StoreFileResponse store(File file, StoreFileRequest request) throws StoreFileException {
        try {
            return store(new FileInputStream(file), request);
        } catch (FileNotFoundException e) {
            throw new FastdfsStoreException(file.getName() + " not found.", e);
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

        String group_name = filename.split(":")[0];
        String remote_filename = filename.split(":")[0];

        try {
            FileInfo fileInfo = client.get_file_info(group_name, remote_filename);
            NameValuePair[] metadata = client.get_metadata(group_name, remote_filename);

            if (fileInfo == null) {
                return null;
            }


            DefaultStoredFileObject fileObject = new DefaultStoredFileObject();

            buildStoreFileObject(metadata, fileObject);
            fileObject.getAttributes().put("fastdfs-group", group_name);

            fileObject.setStoredFilename(filename);
            fileObject.setProviderName(PROVIDER_NAME);
            fileObject.setImplementation(new FastFile(group_name,
                                                      remote_filename,
                                                      client));

            return fileObject;
        } catch (Throwable e) {
            logger.error(String.format("Delete the file [%s] failed.", filename), e);
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
        String remote_filename = filename.split(":")[0];
        try {
            FileInfo fileInfo = client.get_file_info(group_name, remote_filename);
            NameValuePair[] metadata = client.get_metadata(group_name, remote_filename);

            if (fileInfo == null) {
                return null;
            }

            DefaultStoredFileObject fileObject = new DefaultStoredFileObject();
            buildStoreFileObject(metadata, fileObject);
            fileObject.getAttributes().put("fastdfs-group", group_name);

            fileObject.setStoredFilename(filename);
            fileObject.setProviderName(PROVIDER_NAME);
            fileObject.setImplementation(null);


            client.delete_file(group_name, remote_filename);
            logger.info(String.format("The file [%s] is deleted.", filename));

            return fileObject;
        } catch (Throwable e) {
            logger.error(String.format("Delete the file [%s] failed.", filename), e);
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.fastdfsProperties);

        try {
            ClientGlobal.setG_anti_steal_token(fastdfsProperties.isHttpAntiStealToken());
            ClientGlobal.setG_charset(fastdfsProperties.getCharset());
            ClientGlobal.setG_connect_timeout(fastdfsProperties.getConnectTimeoutInseconds());
            ClientGlobal.setG_network_timeout(fastdfsProperties.getNetworkTimeoutInSeconds());
            ClientGlobal.setG_secret_key(fastdfsProperties.getHttpSecretKey());

            TrackerGroup trackerGroup =
                    new TrackerGroup(fastdfsProperties.getTrackerServers()
                                                      .stream()
                                                      .map(server -> {
                                                          try {
                                                              return InetSocketAddress.createUnresolved(
                                                                      server.split(":")[0],
                                                                      Integer.parseInt(server.split(":")[1]));
                                                          } catch (Throwable e) {
                                                              logger.error("Unresolvable tracker server" +
                                                                                   server, e);
                                                          }
                                                          return null;
                                                      })
                                                      .filter(address -> address != null)
                                                      .collect(Collectors.toList())
                                                      .toArray(new InetSocketAddress[]{}));
            ClientGlobal.setG_tracker_group(trackerGroup);
            ClientGlobal.setG_tracker_http_port(fastdfsProperties.getHttpTrackerHttpPort());

            TrackerClient tracker = new TrackerClient();
            this.trackerServer = tracker.getConnection();
            this.storageServer = tracker.getStoreStorage(this.trackerServer);
            this.client = new StorageClient(trackerServer, storageServer);
        } catch (Throwable e) {
            throw new FastdfsStoreException("Fail to initialize FastDFS client.", e);
        }
    }

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
    public void destroy() throws Exception {
        this.trackerServer.close();
        this.storageServer.close();
    }
}

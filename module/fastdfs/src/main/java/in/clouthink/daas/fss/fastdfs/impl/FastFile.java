package in.clouthink.daas.fss.fastdfs.impl;

import in.clouthink.daas.fss.fastdfs.exception.FastdfsDownloadException;
import in.clouthink.daas.fss.util.IOUtils;
import org.csource.common.FastdfsException;
import org.csource.fastdfs.StorageClient;

import java.io.IOException;
import java.io.OutputStream;

public class FastFile {

    private String group;

    private String filename;

    private StorageClient storageClient;

    public FastFile(String group, String filename, StorageClient storageClient) {
        this.group = group;
        this.filename = filename;
        this.storageClient = storageClient;
    }

    public String getGroup() {
        return group;
    }

    public String getFilename() {
        return filename;
    }

    public StorageClient getStorageClient() {
        return storageClient;
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        try {
            byte[] dataInBytes = storageClient.downloadFile(this.group, this.filename);
            //TODO optimize with DownloadCallback
            IOUtils.copy(dataInBytes, outputStream);
        } catch (FastdfsException e) {
            throw new FastdfsDownloadException("Fail to write to output stream", e);
        }
    }

}

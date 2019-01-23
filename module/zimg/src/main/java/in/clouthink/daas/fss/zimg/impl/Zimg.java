package in.clouthink.daas.fss.zimg.impl;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author dz
 * @since 3
 */
public class Zimg {

    private String filename;

    private String downloadEndpoint;

    private ZimgClient zimgClient;

    public Zimg(String filename, String downloadEndpoint, ZimgClient zimgClient) {
        this.filename = filename;
        this.downloadEndpoint = downloadEndpoint;
        this.zimgClient = zimgClient;
    }

    public String getFilename() {
        return filename;
    }

    public String getDownloadEndpoint() {
        return downloadEndpoint;
    }

    public ZimgClient getZimgClient() {
        return zimgClient;
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        zimgClient.download(filename, downloadEndpoint, outputStream);
    }

}

package in.clouthink.daas.fss.webdav.impl;

import com.github.sardine.Sardine;
import in.clouthink.daas.fss.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class WebDavFile {

    private String remoteFilename;

    private Sardine sardine;

    public WebDavFile(String remoteFilename, Sardine sardine) {
        this.remoteFilename = remoteFilename;
        this.sardine = sardine;
    }

    public String getRemoteFilename() {
        return remoteFilename;
    }

    public Sardine getSardine() {
        return sardine;
    }

    public void writeTo(OutputStream outputStream, int bufferSize) throws IOException {
        InputStream inputStream = sardine.get(remoteFilename);
        try {
            IOUtils.copy(inputStream, outputStream, bufferSize);
        } finally {
            IOUtils.close(inputStream);
        }
    }

}

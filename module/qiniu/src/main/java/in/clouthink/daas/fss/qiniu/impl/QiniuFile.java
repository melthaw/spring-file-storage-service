package in.clouthink.daas.fss.qiniu.impl;

import com.qiniu.util.Auth;
import in.clouthink.daas.fss.util.IOUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * @author dz
 * @since 3
 */
public class QiniuFile {

    private String url;

    private Auth auth;

    public QiniuFile(String url, Auth auth) {
        this.url = url;
        this.auth = auth;
    }

    public String getUrl() {
        return url;
    }

    public Auth getAuth() {
        return auth;
    }

    public void writeTo(OutputStream outputStream, int bufferSize) throws IOException {
        String downloadUrl = this.auth.privateDownloadUrl(this.url, 3600L);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(downloadUrl).build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Failed to download file: " + response);
        }

        InputStream inputStream = response.body().byteStream();
        IOUtils.copy(inputStream, outputStream, bufferSize);
        IOUtils.close(inputStream);
    }

}

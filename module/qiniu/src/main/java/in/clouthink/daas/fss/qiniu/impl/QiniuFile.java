package in.clouthink.daas.fss.qiniu.impl;

import com.qiniu.util.Auth;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.OutputStream;

public class QiniuFile {

    private String path;

    private Auth auth;

    public QiniuFile(String path, Auth auth) {
        this.path = path;
        this.auth = auth;
    }

    public String getPath() {
        return path;
    }

    public Auth getAuth() {
        return auth;
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        String downloadUrl = this.auth.privateDownloadUrl(path, 3600L);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(downloadUrl).build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Failed to download file: " + response);
        }

        outputStream.write(response.body().bytes());
    }

}

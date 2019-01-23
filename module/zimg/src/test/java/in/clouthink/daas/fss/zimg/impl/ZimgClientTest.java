package in.clouthink.daas.fss.zimg.impl;

import in.clouthink.daas.fss.zimg.client.ZimgResult;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ZimgClientTest {

    private ZimgClient zimgClient = new ZimgClient();

    private String storedFilename;

    //    @Test
    public void testUpload() {
        File file = new File("/Users/dz/project/mimilu.studio/1.jpg");
        ZimgResult result = zimgClient.upload(file, "jpeg1", "http://127.0.0.1:4869/upload");
        Assert.assertEquals(true, result.isRet());
    }

        @Test
    public void testDelete() {
        zimgClient.delete("ok", "http://127.0.0.1:4869/admin");
    }

    @Test
    public void testDownload() throws IOException {
        zimgClient.download("2db146432e77efdb04ae4e7537600939", "http://127.0.0.1:4869", new FileOutputStream(new File("/tmp/hello.jpg")));
    }

}

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

    @Test
    public void testAll() throws IOException {
        test1Upload();
        test2Download();
        test3Delete();
    }


    public void test1Upload() {
        File file = new File("/Users/dz/project/mimilu.studio/1.jpg");
        ZimgResult result = zimgClient.upload(file, "jpeg", "http://127.0.0.1:4869/upload");
        Assert.assertEquals(true, result.isRet());
        storedFilename = result.getInfo().getMd5();
    }

    public void test2Download() throws IOException {
        zimgClient.download(storedFilename,
                            "http://127.0.0.1:4869",
                            new FileOutputStream(new File("/tmp/hello.jpg")));
    }

    public void test3Delete() {
        zimgClient.delete(storedFilename, "http://127.0.0.1:4869/admin");
    }

}

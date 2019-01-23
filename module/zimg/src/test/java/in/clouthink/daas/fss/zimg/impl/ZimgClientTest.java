package in.clouthink.daas.fss.zimg.impl;

import in.clouthink.daas.fss.zimg.client.ZimgResult;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class ZimgClientTest {

    private ZimgClient zimgClient = new ZimgClient();

    private String storedFilename;


//    @Test
    public void testUpload() {
        File file = new File("/Users/dz/project/mimilu.studio/1.jpg");
        ZimgResult result = zimgClient.upload(file,"jpeg1", "http://127.0.0.1:4869/upload");
        Assert.assertEquals(true, result.isRet());
    }

    @Test
    public void testDelete() {
         zimgClient.delete("ok", "http://127.0.0.1:4869/admin");
    }


}

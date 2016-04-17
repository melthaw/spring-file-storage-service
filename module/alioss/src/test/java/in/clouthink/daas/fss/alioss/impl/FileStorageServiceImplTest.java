package in.clouthink.daas.fss.alioss.impl;

import in.clouthink.daas.fss.alioss.TestContext;
import in.clouthink.daas.fss.alioss.builder.OssFileObjectBuilder;
import in.clouthink.daas.fss.alioss.model.DefaultFileStorageMetadata;
import in.clouthink.daas.fss.alioss.model.OssFileObject;
import in.clouthink.daas.fss.core.FileObject;
import in.clouthink.daas.fss.core.FileStorage;
import in.clouthink.daas.fss.core.FileStorageMetadata;
import in.clouthink.daas.fss.spi.FileStorageService;
import in.clouthink.daas.fss.util.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.*;

import static org.junit.Assert.*;

/**
 * Created by LiangBin on 16/4/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestContext.class)
public class FileStorageServiceImplTest {
    
    @Autowired
    private FileStorageService fileStorageService;
    
    @Test
    public void testGetStorageMetadata() throws Exception {
        FileStorageMetadata metadata = fileStorageService.getStorageMetadata();
        assertTrue(metadata instanceof DefaultFileStorageMetadata);
    }
    
    @Test
    public void testStore() throws Exception {
        String filename = "vm-sync.txt";
        String folder = "/Users/LiangBin/Test/";
        String path = folder + filename;
        InputStream is = new FileInputStream(path);
        OssFileObject request = OssFileObjectBuilder.create()
                                                    .valid()
                                                    .withOriginalFilename(filename)
                                                    .withId(null)
                                                    .build();
        FileStorage fileStorage = fileStorageService.store(is, request);
        assertNotNull(fileStorage);
        FileObject fileObject = fileStorage.getFileObject();
        assertNotNull(fileObject);
        assertNotNull(fileObject.getId());
        assertEquals(fileObject.getId(), fileObject.getFinalFilename());
        assertEquals(request.getOriginalFilename(),
                     fileObject.getOriginalFilename());
        assertNotNull(fileObject.getUploadedAt());
        
        File savedFile = new File(folder + fileObject.getFinalFilename());
        OutputStream os = new FileOutputStream(savedFile);
        fileStorage.writeTo(os, 1024);
        os.flush();
        IOUtils.close(os);
        assertTrue(savedFile.exists());
        assertTrue(savedFile.length() > 0);
    }
    
    @Test
    public void testRestore() throws Exception {
    
    }
    
    @Test
    public void testFindById() throws Exception {
    
    }
    
    @Test
    public void testFindByFilename() throws Exception {
    
    }
}

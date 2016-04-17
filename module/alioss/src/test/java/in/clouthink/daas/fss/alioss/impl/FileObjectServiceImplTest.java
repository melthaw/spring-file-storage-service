package in.clouthink.daas.fss.alioss.impl;

import in.clouthink.daas.fss.alioss.TestContext;
import in.clouthink.daas.fss.alioss.builder.OssFileObjectBuilder;
import in.clouthink.daas.fss.alioss.model.OssFileObject;
import in.clouthink.daas.fss.core.FileObject;
import in.clouthink.daas.fss.core.FileStorage;
import in.clouthink.daas.fss.spi.FileObjectService;
import in.clouthink.daas.fss.spi.FileStorageService;
import junit.framework.TestCase;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by LiangBin on 16/4/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestContext.class)
public class FileObjectServiceImplTest {
    
    @Autowired
    private FileObjectService fileObjectService;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    private List<String> clearList = new ArrayList<String>();
    
    private FileStorage uploadFile() throws FileNotFoundException {
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
        clearList.add(fileStorage.getFileObject().getId());
        return fileStorage;
    }
    
    @Test
    public void testSave() throws Exception {
    }
    
    @Test
    public void testFindById() throws Exception {
        String fileKey = RandomStringUtils.randomAlphabetic(59);
        FileObject fileObject = fileObjectService.findById(fileKey);
        assertNull(fileObject);
        
        fileKey = uploadFile().getFileObject().getId();
        fileObject = fileObjectService.findById(fileKey);
        assertNotNull(fileObject);
        assertEquals(fileKey, fileObject.getId());
        assertEquals(fileKey, fileObject.getFinalFilename());
    }
    
    @Test
    public void testFindByFinalFilename() throws Exception {
    
    }
    
    @Test
    public void testDeleteById() throws Exception {
    
    }
    
    @Test
    public void testDeleteByFinalFilename() throws Exception {
    
    }
    
    @Test
    public void testSaveAsHistory() throws Exception {
    
    }
    
    @Test
    public void testFindHistoryById() throws Exception {
    
    }
    
    @After
    public void tearDown() {
        for (String key : clearList) {
            fileObjectService.deleteById(key);
        }
    }
}

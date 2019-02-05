package in.clouthink.daas.fss.glusterfs.impl;

import in.clouthink.daas.fss.core.*;
import in.clouthink.daas.fss.glusterfs.exception.GlusterfsStoreException;
import in.clouthink.daas.fss.glusterfs.support.GlusterfsProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.io.*;

/**
 * @author LiangBin & dz
 */
public class FileStorageImpl implements FileStorage, InitializingBean {

    private static final Log logger = LogFactory.getLog(FileStorageImpl.class);

    public static final String PROVIDER_NAME = "glusterfs";

    @Autowired
    private GlusterfsProperties glusterfsProperties;

    public GlusterfsProperties getGlusterfsProperties() {
        return glusterfsProperties;
    }

    @Override
    public String getName() {
        return PROVIDER_NAME;
    }

    @Override
    public boolean isMetadataSupported() {
        return true;
    }

    @Override
    public StoreFileResponse store(InputStream inputStream, StoreFileRequest request) throws StoreFileException {

        //TODO
        return null;
    }

    @Override
    public StoreFileResponse store(File file, StoreFileRequest request) throws StoreFileException {
        try {
            return store(new FileInputStream(file), request);
        } catch (FileNotFoundException e) {
            throw new GlusterfsStoreException(file.getName() + " not found.", e);
        }
    }

    @Override
    public StoreFileResponse store(byte[] bytes, StoreFileRequest request) throws StoreFileException {
        return store(new ByteArrayInputStream(bytes), request);
    }

    @Override
    public StoredFileObject findByStoredFilename(String filename) {


        //TODO
        return null;

    }

    @Override
    public StoredFileObject delete(String filename) {


        //TODO
        return null;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(glusterfsProperties);


    }

}

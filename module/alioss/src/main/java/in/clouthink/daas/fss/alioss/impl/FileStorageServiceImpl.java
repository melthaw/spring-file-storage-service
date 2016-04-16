package in.clouthink.daas.fss.alioss.impl;

import in.clouthink.daas.fss.core.FileStorage;
import in.clouthink.daas.fss.core.FileStorageMetadata;
import in.clouthink.daas.fss.core.FileStorageRequest;
import in.clouthink.daas.fss.spi.FileStorageService;

import java.io.InputStream;

/**
 * Created by LiangBin on 16/4/16.
 */
public class FileStorageServiceImpl implements FileStorageService {
    
    @Override
    public FileStorageMetadata getStorageMetadata() {
        return null;
    }
    
    @Override
    public FileStorage store(InputStream inputStream,
                             FileStorageRequest request) {
        return null;
    }
    
    @Override
    public FileStorage restore(String previousId,
                               InputStream inputStream,
                               FileStorageRequest request) {
        return null;
    }
    
    @Override
    public FileStorage findById(String id) {
        return null;
    }
    
    @Override
    public FileStorage findByFilename(String filename) {
        return null;
    }
}

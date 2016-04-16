package in.clouthink.daas.fss.alioss.impl;

import in.clouthink.daas.fss.core.FileObject;
import in.clouthink.daas.fss.core.FileObjectHistory;
import in.clouthink.daas.fss.spi.FileObjectService;

import java.util.List;

/**
 * Created by LiangBin on 16/4/16.
 */
public class FileObjectServiceImpl implements FileObjectService {
    
    @Override
    public FileObject save(FileObject fileObject) {
        return null;
    }
    
    @Override
    public FileObject findById(String id) {
        return null;
    }
    
    @Override
    public FileObject findByFinalFilename(String finalFileName) {
        return null;
    }
    
    @Override
    public FileObject deleteById(String id) {
        return null;
    }
    
    @Override
    public FileObject deleteByFinalFilename(String finalFileName) {
        return null;
    }
    
    @Override
    public FileObjectHistory saveAsHistory(FileObject fileObject) {
        return null;
    }
    
    @Override
    public List<FileObjectHistory> findHistoryById(String fileObjectId) {
        return null;
    }
}

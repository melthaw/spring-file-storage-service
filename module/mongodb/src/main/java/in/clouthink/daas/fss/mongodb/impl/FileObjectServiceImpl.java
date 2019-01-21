package in.clouthink.daas.fss.mongodb.impl;

import in.clouthink.daas.fss.core.StoreFileRequest;
import in.clouthink.daas.fss.domain.model.FileObject;
import in.clouthink.daas.fss.domain.model.FileObjectHistory;
import in.clouthink.daas.fss.domain.request.FileObjectSearchRequest;
import in.clouthink.daas.fss.domain.service.FileObjectService;
import org.springframework.data.domain.Page;

import java.util.List;

public class FileObjectServiceImpl implements FileObjectService {

    @Override
    public FileObject findById(String id) {
        return null;
    }

    @Override
    public FileObject findByStoredFilename(String storedFileName) {
        return null;
    }

    @Override
    public List<FileObjectHistory> findHistoryByFileObjectId(String id) {
        return null;
    }

    @Override
    public FileObject merge(StoreFileRequest request, FileObject fileObject) {
        return null;
    }

    @Override
    public FileObjectHistory saveAsHistory(FileObject fileObject) {
        return null;
    }

    @Override
    public FileObject deleteById(String id) {
        return null;
    }

    @Override
    public FileObject deleteByStoredFilename(String storedFileName) {
        return null;
    }

    @Override
    public Page<FileObject> search(FileObjectSearchRequest searchRequest) {
        return null;
    }
}

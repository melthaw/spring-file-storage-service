package in.clouthink.daas.fss.mongodb.impl;

import in.clouthink.daas.fss.core.StoreFileRequest;
import in.clouthink.daas.fss.domain.model.FileObject;
import in.clouthink.daas.fss.domain.model.FileObjectHistory;
import in.clouthink.daas.fss.domain.request.FileObjectSearchRequest;
import in.clouthink.daas.fss.domain.service.FileObjectService;
import in.clouthink.daas.fss.mongodb.repository.FileObjectHistoryRepository;
import in.clouthink.daas.fss.mongodb.repository.FileObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileObjectServiceImpl implements FileObjectService {

    @Autowired
    private FileObjectHistoryRepository fileObjectHistoryRepository;

    @Autowired
    private FileObjectRepository fileObjectRepository;

    @Override
    public FileObject findById(String id) {
        return fileObjectRepository.findById(id);
    }

    @Override
    public FileObject findByStoredFilename(String storedFileName) {
        return fileObjectRepository.findByStoredFilename(storedFileName);
    }

    @Override
    public List<FileObjectHistory> findHistoryByFileObjectId(String id) {
        return fileObjectHistoryRepository.findByFileObjectId(id)
                                          .stream()
                                          .map(item -> (FileObjectHistory) item)
                                          .collect(Collectors
                                                           .toList());
    }

    @Override
    public FileObject merge(StoreFileRequest request, FileObject fileObject) {
        return null;
    }

    @Override
    public FileObject save(StoreFileRequest storeFileRequest) {
        FileObject fileObject = FileObject
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

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
        in.clouthink.daas.fss.mongodb.model.FileObject fileObject =
                in.clouthink.daas.fss.mongodb.model.FileObject.from(storeFileRequest);

        return fileObjectRepository.save(fileObject);
    }

    @Override
    public FileObjectHistory saveAsHistory(FileObject fileObject) {
        in.clouthink.daas.fss.mongodb.model.FileObject mongdbFileObject =
                (in.clouthink.daas.fss.mongodb.model.FileObject) fileObject;

        in.clouthink.daas.fss.mongodb.model.FileObjectHistory result =
                in.clouthink.daas.fss.mongodb.model.FileObjectHistory.from(mongdbFileObject);

        return fileObjectHistoryRepository.save(result);
    }

    @Override
    public FileObject deleteById(String id) {
        in.clouthink.daas.fss.mongodb.model.FileObject result = fileObjectRepository.findById(id);
        if (result == null) {
            return null;
        }

        fileObjectHistoryRepository.deleteByFileObjectId(id);
        fileObjectRepository.delete(result);

        return result;
    }

    @Override
    public FileObject deleteByStoredFilename(String storedFileName) {
        in.clouthink.daas.fss.mongodb.model.FileObject result = fileObjectRepository.findByStoredFilename(storedFileName);
        if (result == null) {
            return null;
        }

        fileObjectHistoryRepository.deleteByFileObject(result);
        fileObjectRepository.delete(result);

        return result;
    }

    @Override
    public Page<FileObject> search(FileObjectSearchRequest searchRequest) {
        return fileObjectRepository.findPage((in.clouthink.daas.fss.mongodb.model.FileObjectSearchRequest) searchRequest)
                                   .map(item -> (FileObject) item);
    }
}

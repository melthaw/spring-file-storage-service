package in.clouthink.daas.fss.web;

import in.clouthink.daas.fss.core.*;
import in.clouthink.daas.fss.repackage.org.apache.commons.io.FilenameUtils;
import in.clouthink.daas.fss.support.DefaultStoreFileRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public class StorePipe {

    private MultipartFile multipartFile;

    private FileStorage fileStorage;

    private String requestBy;

    private String prettyName;

    private Map<String, String> metadata;

    private StoreSuccessHandler successHandler;

    private StoreFailureHandler failureHandler;

    public StorePipe source(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
        return this;
    }

    public StorePipe target(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
        return this;
    }

    public StorePipe success(StoreSuccessHandler handler) {
        this.successHandler = handler;
        return this;
    }

    public StorePipe failed(StoreFailureHandler handler) {
        this.failureHandler = handler;
        return this;
    }

    public StorePipe requestBy(String requestBy) {
        this.requestBy = requestBy;
        return this;
    }

    public StorePipe prettyName(String prettyName) {
        this.prettyName = prettyName;
        return this;
    }

    public StorePipe metadata(Map<String, String> metadata) {
        this.metadata = metadata;
        return this;
    }

    public void store() throws IOException {
        StoreFileRequest request = buildFileStorageRequest(multipartFile, metadata);
        try {
            StoreFileResponse response = fileStorage.store(multipartFile.getInputStream(), request);
            if (this.successHandler != null) {
                this.successHandler.onStoreSuccess(request, response);
            }
        } catch (StoreFileException exception) {
            if (this.failureHandler != null) {
                this.failureHandler.onStoreFailure(request, exception);
            }
            else {
                throw exception;
            }
        }
    }

    private StoreFileRequest buildFileStorageRequest(MultipartFile multipartFile,
                                                     Map<String, String> uploadFileRequest) {
        DefaultStoreFileRequest result = new DefaultStoreFileRequest();
        result.setSize(multipartFile.getSize());
        result.setUploadedBy(this.requestBy);
        result.setAttributes(uploadFileRequest);

        String originalFileName = multipartFile.getOriginalFilename();
        result.setOriginalFilename(originalFileName);

        String prettyFilename = originalFileName;

        String originalSuffix = FilenameUtils.getExtension(originalFileName);

        if (!StringUtils.isEmpty(this.prettyName) &&
                !StringUtils.isEmpty(originalSuffix) &&
                !this.prettyName.endsWith("." + originalSuffix)) {
            prettyFilename = FilenameUtils.getPrefix(this.prettyName) + "." + originalSuffix;
        }

        result.setPrettyFilename(prettyFilename);

        String contentType = multipartFile.getContentType();
        if (StringUtils.isEmpty(contentType)) {
            // Fixed ContentType from IE
            contentType = multipartFile.getContentType();
            if ("image/pjpeg".equals(contentType) || "image/jpg".equals(contentType)) {
                contentType = "image/jpeg";
            }
            else if ("image/x-png".equals(contentType)) {
                contentType = "image/png";
            }
        }
        result.setContentType(contentType);

        return result;
    }

}

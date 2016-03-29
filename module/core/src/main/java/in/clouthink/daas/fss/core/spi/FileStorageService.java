package in.clouthink.daas.fss.core.spi;

import in.clouthink.daas.fss.core.FileStorage;
import in.clouthink.daas.fss.core.FileStorageMetadata;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by dz on 16/3/28.
 */
public interface FileStorageService {

	FileStorageMetadata getStorageMetadata();

	FileStorage store(MultipartFile file, String byWho);

	FileStorage findById(String id);

	FileStorage findByFilename(String filename);

}

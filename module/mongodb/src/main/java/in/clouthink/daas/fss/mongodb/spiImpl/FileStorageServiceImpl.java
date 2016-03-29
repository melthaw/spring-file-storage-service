package in.clouthink.daas.fss.mongodb.spiImpl;

import in.clouthink.daas.fss.core.FileStorage;
import in.clouthink.daas.fss.core.FileStorageMetadata;
import in.clouthink.daas.fss.core.spi.FileStorageService;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by dz on 16/3/29.
 */
public class FileStorageServiceImpl implements FileStorageService {

	@Override
	public FileStorageMetadata getStorageMetadata() {
		return null;
	}

	@Override
	public FileStorage store(MultipartFile file, String byWho) {
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

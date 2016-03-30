package in.clouthink.daas.fss.mongodb.spiImpl;

import com.mongodb.gridfs.GridFSDBFile;
import in.clouthink.daas.fss.core.FileObject;
import in.clouthink.daas.fss.core.FileStorage;
import in.clouthink.daas.fss.mongodb.service.GridFSDBFileProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by dz on 16/3/29.
 */
public class FileStorageImpl implements FileStorage {

	private static final Log logger = LogFactory.getLog(FileStorageImpl.class);

	private FileObject fileObject;

	private GridFSDBFileProvider gridFSDBFileProvider;

	public FileStorageImpl() {
	}

	public FileStorageImpl(FileObject fileObject, GridFSDBFileProvider gridFSDBFileProvider) {
		this.fileObject = fileObject;
		this.gridFSDBFileProvider = gridFSDBFileProvider;
	}

	@Override
	public FileObject getFileObject() {
		return this.fileObject;
	}

	@Override
	public GridFSDBFile getImplementation() {
		return gridFSDBFileProvider.getGridFSDBFile();
	}

	@Override
	public void writeTo(OutputStream outputStream, long bufferSize) throws IOException {
		logger.warn(String.format("The bufferSize is not supported in current provider, the value[%d] will be ignored.",
								  bufferSize));
		getImplementation().writeTo(outputStream);
	}


}

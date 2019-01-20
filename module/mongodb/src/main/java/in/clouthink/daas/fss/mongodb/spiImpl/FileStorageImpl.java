package in.clouthink.daas.fss.mongodb.spiImpl;

import com.mongodb.gridfs.GridFSDBFile;
import in.clouthink.daas.fss.domain.model.FileObject;
import in.clouthink.daas.fss.core.FileStorage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author dz
 */
public class FileStorageImpl implements FileStorage {

	private static final Log logger = LogFactory.getLog(FileStorageImpl.class);

	private FileObject fileObject;

	private GridFSDBFile gridFSDBFile;

	public FileStorageImpl() {
	}

	public FileStorageImpl(FileObject fileObject, GridFSDBFile gridFSDBFile) {
		this.fileObject = fileObject;
		this.gridFSDBFile = gridFSDBFile;
	}

	@Override
	public FileObject getFileObject() {
		return this.fileObject;
	}

	@Override
	public GridFSDBFile getImplementation() {
		return gridFSDBFile;
	}

	@Override
	public void writeTo(OutputStream outputStream, long bufferSize) throws IOException {
		logger.warn(String.format("The bufferSize is not supported in current provider, the value[%d] will be ignored.",
								  bufferSize));
		getImplementation().writeTo(outputStream);
	}

}

package in.clouthink.daas.fss.alioss.spiImpl;

import com.aliyun.oss.model.OSSObject;
import in.clouthink.daas.fss.core.FileObject;
import in.clouthink.daas.fss.core.FileStorage;
import in.clouthink.daas.fss.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author LiangBin & dz
 */
public class FileStorageImpl implements FileStorage {

	private FileObject fileObject;

	private OSSObject ossObject;

	public FileStorageImpl(FileObject fileObject, OSSObject ossObject) {
		this.fileObject = fileObject;
		this.ossObject = ossObject;
	}

	@Override
	public FileObject getFileObject() {
		return fileObject;
	}

	@Override
	public OSSObject getImplementation() {
		return ossObject;
	}

	@Override
	public void writeTo(OutputStream outputStream, long bufferSize) throws IOException {
		if (bufferSize <= 0) {
			bufferSize = 1024 * 4;
		}
		InputStream is = ossObject.getObjectContent();
		int length = -1;
		byte[] bytes = new byte[(int) bufferSize];
		while ((length = is.read(bytes)) > 0) {
			outputStream.write(bytes, 0, length);
		}
		IOUtils.close(is);
	}

}

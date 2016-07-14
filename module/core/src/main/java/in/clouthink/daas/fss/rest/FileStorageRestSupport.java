package in.clouthink.daas.fss.rest;

import in.clouthink.daas.fss.core.FileObject;
import in.clouthink.daas.fss.core.FileObjectHistory;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 */
public interface FileStorageRestSupport {

	FileObjectDescriptor getFileObjectDetail(@PathVariable String id) throws IOException;

	FileObject upload(UploadFileRequest uploadFileRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException;

	FileObject reupload(@PathVariable String id,
						UploadFileRequest uploadFileRequest,
						HttpServletRequest request,
						HttpServletResponse response) throws IOException;

	void downloadById(@PathVariable String id, HttpServletResponse response) throws IOException;

	void downloadByFilename(@PathVariable String filename, HttpServletResponse response) throws IOException;

}

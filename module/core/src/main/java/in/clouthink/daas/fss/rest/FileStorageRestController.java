package in.clouthink.daas.fss.rest;

import in.clouthink.daas.fss.core.FileObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

/**
 */
@Controller
@RequestMapping(value = "/api")
public class FileStorageRestController {

	@Autowired
	private FileStorageRestSupport sharedFileStorageRestSupport;

	@RequestMapping(value = "/files/{id}", method = RequestMethod.GET)
	@ResponseBody
	public FileObjectDescriptor getFileObjectDetail(@PathVariable String id,
													HttpServletRequest request,
													HttpServletResponse response) throws IOException {
		return sharedFileStorageRestSupport.getFileObjectDetail(id);
	}

	@RequestMapping(value = "/files", method = RequestMethod.POST)
	@ResponseBody
	public String upload(UploadFileRequest uploadFileRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		FileObject fileObject = sharedFileStorageRestSupport.upload(uploadFileRequest, request, response);
		return fileObject.getId();
	}

	@RequestMapping(value = "/files/{id}", method = RequestMethod.POST)
	@ResponseBody
	public void reupload(@PathVariable String id,
						 UploadFileRequest uploadFileRequest,
						 HttpServletRequest request,
						 HttpServletResponse response) throws IOException {
		sharedFileStorageRestSupport.reupload(id, uploadFileRequest, request, response);
	}

	@RequestMapping(value = "/files/{id}/download", method = RequestMethod.GET)
	@ResponseBody
	public void downloadById(@PathVariable String id, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		sharedFileStorageRestSupport.downloadById(id, response);
	}

	@RequestMapping(value = "/download/{filename:.+}", method = RequestMethod.GET)
	@ResponseBody
	public void downloadByFilename(@PathVariable String filename,
								   HttpServletRequest request,
								   HttpServletResponse response) throws IOException {
		sharedFileStorageRestSupport.downloadByFilename(filename, response);
	}

}

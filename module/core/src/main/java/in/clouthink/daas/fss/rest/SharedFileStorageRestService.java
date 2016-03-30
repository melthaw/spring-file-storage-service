package in.clouthink.daas.fss.rest;

import in.clouthink.daas.fss.core.FileObject;
import in.clouthink.daas.fss.core.FileObjectHistory;
import in.clouthink.daas.fss.spi.FileObjectService;
import in.clouthink.daas.fss.spi.FileStorageService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 */
@Controller
@RequestMapping(value = "/api")
public class SharedFileStorageRestService implements InitializingBean {

	@Autowired
	private FileObjectService fileObjectService;

	@Autowired
	private FileStorageService fileStorageService;

	private FileStorageRestSupport fileStorageRestSupport;

	@RequestMapping(value = "/files/{id}/object", method = RequestMethod.POST)
	@ResponseBody
	public FileObject getFileObjectDetail(@PathVariable String id,
										  HttpServletRequest request,
										  HttpServletResponse response) throws IOException {
		return fileObjectService.findById(id);
	}

	@RequestMapping(value = "/files/{id}/history", method = RequestMethod.POST)
	@ResponseBody
	public List<FileObjectHistory> getFileObjectHistory(@PathVariable String id,
														HttpServletRequest request,
														HttpServletResponse response) throws IOException {
		return fileObjectService.findHistoryById(id);
	}

	@RequestMapping(value = "/files/{id}", method = RequestMethod.POST)
	@ResponseBody
	public void reupload(@PathVariable String id,
						 @RequestBody UploadFileRequest uploadFileRequest,
						 HttpServletRequest request,
						 HttpServletResponse response) throws IOException {
		fileStorageRestSupport.reupload(id, uploadFileRequest, request, response);
	}

	@RequestMapping(value = "/files", method = RequestMethod.POST)
	@ResponseBody
	public String upload(@RequestBody UploadFileRequest uploadFileRequest,
						 HttpServletRequest request,
						 HttpServletResponse response) throws IOException {
		FileObject fileObject = fileStorageRestSupport.upload(uploadFileRequest, request, response);
		return fileObject.getId();
	}

	@RequestMapping(value = "/files/{id}", method = RequestMethod.GET)
	@ResponseBody
	public void downloadById(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) throws
																												IOException {
		fileStorageRestSupport.downloadById(id, response);
	}

	@RequestMapping(value = "/download/{filename:.+}", method = RequestMethod.GET)
	@ResponseBody
	public void downloadByFilename(@PathVariable String filename,
								   HttpServletRequest request,
								   HttpServletResponse response) throws IOException {
		fileStorageRestSupport.downloadByFilename(filename, response);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(fileObjectService);
		Assert.notNull(fileStorageService);
		fileStorageRestSupport = new FileStorageRestSupport(fileObjectService, fileStorageService);
	}

}

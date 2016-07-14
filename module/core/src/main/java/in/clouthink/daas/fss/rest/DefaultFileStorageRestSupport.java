package in.clouthink.daas.fss.rest;

import in.clouthink.daas.fss.core.*;
import in.clouthink.daas.fss.repackage.org.apache.commons.io.FilenameUtils;
import in.clouthink.daas.fss.spi.FileObjectService;
import in.clouthink.daas.fss.spi.FileStorageService;
import in.clouthink.daas.fss.util.HttpMultipartUtils;
import in.clouthink.daas.fss.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author dz
 */
@Service
public class DefaultFileStorageRestSupport implements FileStorageRestSupport {

	@Autowired
	private FileObjectService fileObjectService;

	@Autowired
	private FileStorageService fileStorageService;

	@Override
	public FileObjectDescriptor getFileObjectDetail(@PathVariable String id) throws IOException {
		FileObject fileObject = fileObjectService.findById(id);
		Map<String,Object> extraAttributes = fileStorageService.buildExtraAttributes(fileObject);
		return DefaultFileObjectDescriptor.from(fileObjectService.findById(id), extraAttributes);
	}

	@Override
	public FileObject upload(UploadFileRequest uploadFileRequest,
							 HttpServletRequest request,
							 HttpServletResponse response) throws IOException {
		if (StringUtils.isEmpty(uploadFileRequest.getUploadedBy())) {
			throw new FileStorageException("上传附件的用户不能为空.");
		}

		MultipartFile multipartFile = HttpMultipartUtils.resolveMultipartFile(request);
		if (multipartFile == null || multipartFile.isEmpty()) {
			throw new FileStorageException("The multipart of http request is required.");
		}

		FileStorageRequest fileStorageRequest = buildFileStorageRequest(multipartFile, uploadFileRequest);

		InputStream is = multipartFile.getInputStream();
		try {
			FileStorage fileStorage = fileStorageService.store(is, fileStorageRequest);
			return fileStorage.getFileObject();
		}
		finally {
			IOUtils.close(is);
		}

	}

	@Override
	public FileObject reupload(@PathVariable String id,
							   UploadFileRequest uploadFileRequest,
							   HttpServletRequest request,
							   HttpServletResponse response) throws IOException {
		if (StringUtils.isEmpty(uploadFileRequest.getUploadedBy())) {
			throw new FileStorageException("上传附件的用户不能为空.");
		}

		MultipartFile multipartFile = HttpMultipartUtils.resolveMultipartFile(request);
		if (multipartFile == null || multipartFile.isEmpty()) {
			throw new FileStorageException("The multipart of http request is required.");
		}

		FileStorageRequest fileStorageRequest = buildFileStorageRequest(multipartFile, uploadFileRequest);

		InputStream is = multipartFile.getInputStream();
		try {
			FileStorage fileStorage = fileStorageService.restore(id, is, fileStorageRequest);
			return fileStorage.getFileObject();
		}
		finally {
			IOUtils.close(is);
		}
	}

	@Override
	public void downloadById(@PathVariable String id, HttpServletResponse response) throws IOException {
		FileStorage fileStorage = fileStorageService.findById(id);
		if (fileStorage == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		FileObject fileObject = fileStorage.getFileObject();
		if (fileObject == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		response.setContentType(fileObject.getContentType());
		String downloadFilename = fileStorage.getFileObject().getFinalFilename();
		if (!StringUtils.isEmpty(fileObject.getPrettyFilename())) {
			downloadFilename = new String(fileObject.getPrettyFilename().getBytes("utf-8"), "ISO_8859_1");
		}
		else if (!StringUtils.isEmpty(fileObject.getOriginalFilename())) {
			downloadFilename = new String(fileObject.getOriginalFilename().getBytes("utf-8"), "ISO_8859_1");
		}
		response.addHeader("Content-Disposition", "attachment; filename=\"" + downloadFilename + "\"");
		OutputStream os = response.getOutputStream();
		try {
			fileStorage.writeTo(os, 4 * 1024);
		}
		finally {
			IOUtils.flush(os);
			IOUtils.close(os);
		}
	}

	@Override
	public void downloadByFilename(@PathVariable String filename, HttpServletResponse response) throws IOException {
		FileStorage fileStorage = fileStorageService.findByFilename(filename);
		if (fileStorage == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		FileObject fileObject = fileStorage.getFileObject();
		if (fileObject == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		int lastIndexOfForwardSlash = filename.lastIndexOf("/");
		if (lastIndexOfForwardSlash >= 0) {
			filename = filename.substring(lastIndexOfForwardSlash);
		}

		response.setContentType(fileObject.getContentType());
		response.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		OutputStream os = response.getOutputStream();
		try {
			fileStorage.writeTo(os, 4 * 1024);
		}
		finally {
			IOUtils.flush(os);
			IOUtils.close(os);
		}
	}

	private FileStorageRequest buildFileStorageRequest(MultipartFile multipartFile,
													   UploadFileRequest uploadFileRequest) {
		DefaultFileStorageRequest result = new DefaultFileStorageRequest();
		result.setCategory(uploadFileRequest.getCategory());
		result.setCode(uploadFileRequest.getCode());
		result.setName(uploadFileRequest.getName());
		result.setDescription(uploadFileRequest.getDescription());
		result.setBizId(uploadFileRequest.getBizId());
		result.setUploadedBy(uploadFileRequest.getUploadedBy());
		result.setAttributes(uploadFileRequest.getAttributes());

		String originalFileName = multipartFile.getOriginalFilename();
		result.setOriginalFilename(originalFileName);

		String prettyFilename = uploadFileRequest.getPrettyFilename();
		if (StringUtils.isEmpty(prettyFilename)) {
			prettyFilename = uploadFileRequest.getName();
		}

		if (StringUtils.isEmpty(prettyFilename)) {
			prettyFilename = originalFileName;
		}
		else {
			String originalSuffix = FilenameUtils.getExtension(originalFileName);
			String prettySuffix = FilenameUtils.getExtension(prettyFilename);
			if (originalSuffix != null) {
				if (prettySuffix == null || !prettySuffix.equalsIgnoreCase(originalSuffix)) {
					prettyFilename += '.' + originalSuffix;
				}
			}
		}
		result.setPrettyFilename(prettyFilename);

		String contentType = uploadFileRequest.getContentType();
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
		result.setSize(multipartFile.getSize());

		return result;
	}

}

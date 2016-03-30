package in.clouthink.daas.fss.rest;

import in.clouthink.daas.fss.core.*;
import in.clouthink.daas.fss.repackage.org.apache.commons.io.FilenameUtils;
import in.clouthink.daas.fss.spi.FileObjectService;
import in.clouthink.daas.fss.spi.FileStorageService;
import in.clouthink.daas.fss.util.HttpMultipartUtils;
import in.clouthink.daas.fss.util.IOUtils;
import in.clouthink.daas.fss.util.IdentityUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by dz on 16/3/29.
 */
public class FileStorageRestSupport {

	private FileObjectService fileObjectService;

	private FileStorageService fileStorageService;

	public FileStorageRestSupport(FileObjectService fileObjectService, FileStorageService fileStorageService) {
		this.fileObjectService = fileObjectService;
		this.fileStorageService = fileStorageService;
	}

	/**
	 * @param id                the id of existed file object
	 * @param uploadFileRequest
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public FileObject reupload(String id,
							   UploadFileRequest uploadFileRequest,
							   HttpServletRequest request,
							   HttpServletResponse response) throws IOException {
		validate(uploadFileRequest);
		MultipartFile multipartFile = HttpMultipartUtils.resolveMultipartFile(request);
		if (multipartFile == null || multipartFile.isEmpty()) {
			throw new FileStorageException("The multipart of http request is required.");
		}

		FileStorageRequest fileStorageRequest = buildFileStorageRequest(multipartFile, uploadFileRequest);

		InputStream is = multipartFile.getInputStream();
		try {
			FileStorage fileStorage = fileStorageService.restore(id, is, fileStorageRequest);
			return fileStorage.getFileObject();
		} finally {
			IOUtils.close(is);
		}
	}

	public FileObject upload(UploadFileRequest uploadFileRequest,
							 HttpServletRequest request,
							 HttpServletResponse response) throws IOException {
		validate(uploadFileRequest);

		MultipartFile multipartFile = HttpMultipartUtils.resolveMultipartFile(request);
		if (multipartFile == null || multipartFile.isEmpty()) {
			throw new FileStorageException("The multipart of http request is required.");
		}

		FileStorageRequest fileStorageRequest = buildFileStorageRequest(multipartFile, uploadFileRequest);

		InputStream is = multipartFile.getInputStream();
		try {
			FileStorage fileStorage = fileStorageService.store(is, fileStorageRequest);
			return fileStorage.getFileObject();
		} finally {
			IOUtils.close(is);
		}
	}

	public void downloadById(String id, HttpServletResponse response) throws IOException {
		FileStorage fileStorage = fileStorageService.findById(id);
		if (fileStorage == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		OutputStream os = response.getOutputStream();
		try {
			fileStorage.writeTo(os, 4 * 1024);
		} finally {
			IOUtils.flush(os);
			IOUtils.close(os);
		}
	}

	public void downloadByFilename(String filename, HttpServletResponse response) throws IOException {
		FileStorage fileStorage = fileStorageService.findByFilename(filename);
		if (fileStorage == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		OutputStream os = response.getOutputStream();
		try {
			fileStorage.writeTo(os, 4 * 1024);
		} finally {
			IOUtils.flush(os);
			IOUtils.close(os);
		}
	}


	private void validate(UploadFileRequest uploadFileRequest) {
		if (StringUtils.isEmpty(uploadFileRequest.getName())) {
			throw new FileStorageException("上传附件的名字不能为空.");
		}
		if (StringUtils.isEmpty(uploadFileRequest.getUploadedBy())) {
			throw new FileStorageException("上传附件的用户不能为空.");
		}
	}

	private FileStorageRequest buildFileStorageRequest(MultipartFile multipartFile,
													   UploadFileRequest uploadFileRequest) {
		DefaultFileStorageRequest result = new DefaultFileStorageRequest();
		result.setCode(uploadFileRequest.getCode());
		result.setBizId(uploadFileRequest.getBizId());
		result.setCategory(uploadFileRequest.getCategory());
		result.setUploadedBy(uploadFileRequest.getUploadedBy());
		result.setAttributes(uploadFileRequest.getAttributes());

		String originalFileName = multipartFile.getOriginalFilename();
		result.setOriginalFilename(originalFileName);

		String suffix = FilenameUtils.getExtension(originalFileName);
		String finalFilename = IdentityUtils.generateId();
		if (!StringUtils.isEmpty(suffix)) {
			finalFilename += '.' + suffix;
		}
		result.setFinalFilename(finalFilename);

		String prettyFilename = uploadFileRequest.getName();
		if (StringUtils.isEmpty(prettyFilename)) {
			prettyFilename = originalFileName;
		} else {
			prettyFilename += '.' + suffix;
		}
		result.setPrettyFilename(prettyFilename);

		String contentType = uploadFileRequest.getContentType();
		if (StringUtils.isEmpty(contentType)) {
			// Fixed ContentType from IE
			contentType = multipartFile.getContentType();
			if ("image/pjpeg".equals(contentType) || "image/jpg".equals(contentType)) {
				contentType = "image/jpeg";
			} else if ("image/x-png".equals(contentType)) {
				contentType = "image/png";
			}
		}
		result.setContentType(contentType);

		return result;
	}

}

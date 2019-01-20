package in.clouthink.daas.fss.alioss.service.impl;

import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import in.clouthink.daas.edm.Edms;
import in.clouthink.daas.edm.EventListener;
import in.clouthink.daas.fss.alioss.exception.AliossStoreException;
import in.clouthink.daas.fss.alioss.support.OssService;
import in.clouthink.daas.fss.alioss.util.FileObjectUtils;
import in.clouthink.daas.fss.core.*;
import in.clouthink.daas.fss.domain.model.FileObject;
import in.clouthink.daas.fss.core.FileStorageService;
import in.clouthink.daas.fss.domain.model.MutableFileObject;
import in.clouthink.daas.fss.spi.MutableFileObjectService;
import in.clouthink.daas.fss.support.DefaultFileObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LiangBin & dz
 */
public class FileStorageServiceImpl implements FileStorageService, EventListener<FileObject>, InitializingBean {

	private static final Log logger = LogFactory.getLog(FileStorageServiceImpl.class);

	@Autowired
	private OssService ossService;

	@Autowired
	private MutableFileObjectService fileObjectService;

	@Override
	public Map<String,Object> buildExtraAttributes(FileObject fileObject) {
		if (fileObject == null) {
			return null;
		}
		String bucket = ossService.resolveBucket(fileObject);

		StringBuilder imageUrl = new StringBuilder("http://");
		StringBuilder fileUrl = new StringBuilder("http://");

		imageUrl.append(bucket)
				.append(".")
				.append(ossService.getOssProperties().getImgDomain())
				.append("/")
				.append(fileObject.getStoredFilename());

		fileUrl.append(bucket)
			   .append(".")
			   .append(ossService.getOssProperties().getOssDomain())
			   .append("/")
			   .append(fileObject.getStoredFilename());

		Map<String,Object> result = new HashMap<String,Object>();
		result.put("imageUrl", imageUrl.toString());
		result.put("fileUrl", fileUrl.toString());
		return result;
	}

	@Override
	public FileStorage findById(String id) {
		FileObject fileObject = fileObjectService.findById(id);
		if (fileObject == null) {
			return null;
		}
		OSSObject ossObject = ossService.getOssObject(fileObject);
		return new FileStorageImpl(fileObject, ossObject);
	}

	@Override
	public FileStorage findByFilename(String finalFilename) {
		FileObject fileObject = fileObjectService.findByStoredFilename(finalFilename);
		if (fileObject == null) {
			return null;
		}
		OSSObject ossObject = ossService.getOssObject(fileObject);
		return new FileStorageImpl(fileObject, ossObject);
	}

	@Override
	public FileStorage store(InputStream inputStream, StoreFileRequest request) {
		FileObject fileObject = createDefaultFileObject(request);
		updateOssStoragePart((MutableFileObject) fileObject);
		fileObject = doStore(inputStream, fileObject);
		fileObject = fileObjectService.save(fileObject);

		OSSObject ossObject = ossService.getOssObject(fileObject);
		return new FileStorageImpl(fileObject, ossObject);
	}

	@Override
	public FileStorage restore(String previousId, InputStream inputStream, StoreFileRequest request) {
		FileObject fileObject = fileObjectService.findById(previousId);
		if (fileObject == null) {
			throw new StoreFileException(String.format("The file object[id=%s] is not found.", previousId));
		}
		fileObjectService.saveAsHistory(fileObject);

		fileObjectService.merge(request, fileObject);
		updateOssStoragePart((MutableFileObject) fileObject);
		fileObject = doStore(inputStream, fileObject);
		fileObject = fileObjectService.save(fileObject);

		OSSObject ossObject = ossService.getOssObject(fileObject);
		return new FileStorageImpl(fileObject, ossObject);
	}

	@Override
	public void onEvent(FileObject fileObject) {
		doDeleteFileObject(fileObject);
	}

	private FileObject doStore(InputStream inputStream, FileObject fileObject) {
		String ossBucket = FileObjectUtils.getOssBucket(fileObject);
		if (StringUtils.isEmpty(ossBucket)) {
			throw new AliossStoreException("The oss bucket is not supplied.");
		}
		String ossKey = FileObjectUtils.getOssKey(fileObject);
		if (StringUtils.isEmpty(ossKey)) {
			throw new AliossStoreException("The oss key is not supplied.");
		}

		ObjectMetadata objectMetadata = ossService.createObjectMetadata(fileObject);
		ossService.getOssClient().putObject(ossBucket, ossKey, inputStream, objectMetadata);
		return fileObject;
	}

	private DefaultFileObject createDefaultFileObject(StoreFileRequest request) {
		//validate
		if (StringUtils.isEmpty(request.getOriginalFilename())) {
			throw new StoreFileException("The originalFilename is required.");
		}
		if (StringUtils.isEmpty(request.getUploadedBy())) {
			throw new StoreFileException("The uploadedBy is required.");
		}

		//create file object from request
		DefaultFileObject fileObject = DefaultFileObject.from(request);

		//pretty filename
		if (StringUtils.isEmpty(fileObject.getPrettyFilename())) {
			fileObject.setPrettyFilename(fileObject.getOriginalFilename());
		}

		//version
		fileObject.setVersion(1);

		//uploaded at
		Date uploadedAt = new Date();
		fileObject.setUploadedAt(uploadedAt);

		return fileObject;
	}

	private void updateOssStoragePart(MutableFileObject fileObject) {
		//oss bucket
		String bucket = ossService.resolveBucket(fileObject);
		FileObjectUtils.setOssBucket(fileObject, bucket);

		//oss key & final filename
		String ossKey = ossService.generateKey(fileObject);
		fileObject.setFinalFilename(ossKey); //oss key as the final filename for ali-oss
		FileObjectUtils.setOssKey(fileObject, ossKey);

		//backend
		fileObject.getAttributes().put("fss-provider", "alioss");
	}

	private void doDeleteFileObject(FileObject fileObject) {
		String ossBucket = FileObjectUtils.getOssBucket(fileObject);
		if (StringUtils.isEmpty(ossBucket)) {
			logger.warn("The oss bucket (of pass-in file object) is null");
			return;
		}
		String ossKey = FileObjectUtils.getOssKey(fileObject);
		if (StringUtils.isEmpty(ossKey)) {
			logger.warn("The oss key (of pass-in file object) is null");
			return;
		}
		try {
			ossService.getOssClient().deleteObject(ossBucket, ossKey);
			logger.info(String.format("The oss file object[bucket=%s,key=%s] is deleted.", ossBucket, ossKey));
		}
		catch (Throwable e) {
			logger.error(String.format("Delete the object[bucket=%s,key=%s] failed.", ossBucket, ossKey), e);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Edms.getEdm().register("in.clouthink.daas.fss#delete", this);
	}

}

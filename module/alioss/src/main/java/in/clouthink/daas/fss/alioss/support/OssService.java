package in.clouthink.daas.fss.alioss.support;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import in.clouthink.daas.fss.core.FileObject;
import in.clouthink.daas.fss.core.FileStorageRequest;

/**
 * The oss access service abstraction.
 *
 * @author LiangBin & dz
 */
public interface OssService {

	/**
	 * @return
	 */
	OssProperties getOssProperties();

	/**
	 * The the oss access client
	 *
	 * @return
	 */
	OSSClient getOssClient();

	/**
	 * @param fileObject
	 * @return
	 */
	OSSObject getOssObject(FileObject fileObject);

	/**
	 * Get the bucket that the file requested will store
	 *
	 * @param request
	 * @return
	 */
	String resolveBucket(FileStorageRequest request);

	/**
	 * @param request
	 * @return
	 */
	String generateKey(FileStorageRequest request);

	/**
	 * Create an object meta from a FileStorageRequest
	 *
	 * @param request
	 * @return
	 */
	ObjectMetadata createObjectMetadata(FileStorageRequest request);

}

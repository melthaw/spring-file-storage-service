package in.clouthink.daas.fss.alioss.support;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import in.clouthink.daas.fss.domain.model.FileObject;
import in.clouthink.daas.fss.core.StoreFileRequest;

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
	String resolveBucket(StoreFileRequest request);

	/**
	 * @param request
	 * @return
	 */
	String generateKey(StoreFileRequest request);

	/**
	 * Create an object meta from a FileStorageRequest
	 *
	 * @param request
	 * @return
	 */
	ObjectMetadata createObjectMetadata(StoreFileRequest request);

}

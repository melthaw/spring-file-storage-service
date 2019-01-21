package in.clouthink.daas.fss.zimg.support;

/**
 * Config for zimg client.
 *
 * @author dz
 */
public interface ZimgProperties {

	/**
	 * The zimg upload endpoint
	 */
	String getUploadEndpoint();

	/**
	 * The zimg download endpoint
	 */
	String getDownloadEndpoint();

}

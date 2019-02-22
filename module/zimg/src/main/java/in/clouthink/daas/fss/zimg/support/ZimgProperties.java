package in.clouthink.daas.fss.zimg.support;

/**
 * Config for zimg client.
 *
 * @author dz
 * @since 3
 */
public interface ZimgProperties {

	/**
	 * @return The zimg upload endpoint
	 */
	String getUploadEndpoint();

	/**
	 * @return The zimg download endpoint
	 */
	String getDownloadEndpoint();

	/**
	 *
	 * @return the zimg admin endpoint (note: the request ip should be in whitelist)
	 */
	String getAdminEndpoint();

	/**
	 *
	 * @return the zimg info endpoint to get the metadata
	 */
    String getInfoEndpoint();
}

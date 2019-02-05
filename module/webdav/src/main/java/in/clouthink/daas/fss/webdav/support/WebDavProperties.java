package in.clouthink.daas.fss.webdav.support;

/**
 * Config for WebDav client.
 *
 * @author dz
 */
public interface WebDavProperties {

	String getUsername();

	String getPassword();

	String getUploadEndpoint();

	String getDownloadEndpoint();

}

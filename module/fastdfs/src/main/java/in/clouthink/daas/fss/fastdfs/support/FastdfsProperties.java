package in.clouthink.daas.fss.fastdfs.support;

import java.util.List;

/**
 * Config for fastdfs client.
 *
 * @author dz
 */
public interface FastdfsProperties {

    /**
     * fastdfs.connect_timeout_in_seconds
     */
    int getConnectTimeoutInseconds();

    /**
     * fastdfs.network_timeout_in_seconds
     */
    int getNetworkTimeoutInSeconds();

    /**
     * fastdfs.charset
     */
    String getCharset();

    /**
     * fastdfs.http_anti_steal_token
     */
    boolean isHttpAntiStealToken();

    /**
     * fastdfs.http_secret_key
     */
    String getHttpSecretKey();

    /**
     * fastdfs.http_tracker_http_port
     */
    int getHttpTrackerHttpPort();

    /**
     * fastdfs.tracker_servers
     */
    List<String> getTrackerServers();

}

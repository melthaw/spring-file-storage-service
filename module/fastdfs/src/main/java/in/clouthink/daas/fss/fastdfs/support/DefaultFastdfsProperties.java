package in.clouthink.daas.fss.fastdfs.support;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class DefaultFastdfsProperties implements FastdfsProperties, InitializingBean {

    /**
     * fastdfs.connect_timeout_in_seconds
     */
    int connectTimeoutInseconds = 5;

    /**
     * fastdfs.network_timeout_in_seconds
     */
    int networkTimeoutInSeconds = 5;

    /**
     * fastdfs.charset
     */
    String charset = "UTF-8";

    /**
     * fastdfs.http_anti_steal_token
     */
    boolean httpAntiStealToken = false;

    /**
     * fastdfs.http_secret_key
     */
    String httpSecretKey;

    /**
     * fastdfs.http_tracker_http_port
     */
    int httpTrackerHttpPort = 80;

    /**
     * fastdfs.tracker_servers
     */
    List<String> trackerServers = new ArrayList<>();

    @Override
    public int getConnectTimeoutInseconds() {
        return connectTimeoutInseconds;
    }

    public void setConnectTimeoutInseconds(int connectTimeoutInseconds) {
        this.connectTimeoutInseconds = connectTimeoutInseconds;
    }

    @Override
    public int getNetworkTimeoutInSeconds() {
        return networkTimeoutInSeconds;
    }

    public void setNetworkTimeoutInSeconds(int networkTimeoutInSeconds) {
        this.networkTimeoutInSeconds = networkTimeoutInSeconds;
    }

    @Override
    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    @Override
    public boolean isHttpAntiStealToken() {
        return httpAntiStealToken;
    }

    public void setHttpAntiStealToken(boolean httpAntiStealToken) {
        this.httpAntiStealToken = httpAntiStealToken;
    }

    @Override
    public String getHttpSecretKey() {
        return httpSecretKey;
    }

    public void setHttpSecretKey(String httpSecretKey) {
        this.httpSecretKey = httpSecretKey;
    }

    @Override
    public int getHttpTrackerHttpPort() {
        return httpTrackerHttpPort;
    }

    public void setHttpTrackerHttpPort(int httpTrackerHttpPort) {
        this.httpTrackerHttpPort = httpTrackerHttpPort;
    }

    @Override
    public List<String> getTrackerServers() {
        return trackerServers;
    }

    public void setTrackerServers(List<String> trackerServers) {
        this.trackerServers = trackerServers;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(trackerServers);
        Assert.notEmpty(trackerServers);
    }
}

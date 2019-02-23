package in.clouthink.daas.fss.glusterfs.support;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @author dz
 */
public class DefaultGlusterfsProperties implements GlusterfsProperties, InitializingBean {

    private String server;

    private String volume;

    private int bufferSize = 1024 * 4;

    @Override
    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    @Override
    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    @Override
    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        if (bufferSize < 1024) {
            throw new IllegalArgumentException("The buffer size must be greater than or equal to 1024");
        }
        this.bufferSize = bufferSize;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(server);
        Assert.notNull(volume);
    }

}

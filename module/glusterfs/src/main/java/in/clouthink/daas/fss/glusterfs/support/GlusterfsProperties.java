package in.clouthink.daas.fss.glusterfs.support;

/**
 * Config for glusterfs client.
 *
 * @author dz
 */
public interface GlusterfsProperties {

    String getServer();

    String getVolume();

    int getBufferSize();

}

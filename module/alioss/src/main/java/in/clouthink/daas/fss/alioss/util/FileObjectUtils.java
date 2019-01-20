package in.clouthink.daas.fss.alioss.util;

import in.clouthink.daas.fss.domain.model.FileObject;

/**
 * @author dz
 */
public class FileObjectUtils {

    /**
     * The oss bucket is saved in the extra attributes
     *
     * @param fileObject
     * @return
     */
    public static String getOssBucket(FileObject fileObject) {
        return (String) fileObject.getAttributes().get("oss-bucket");
    }

    /**
     * The oss key is saved in the extra attributes
     *
     * @param fileObject
     * @return
     */
    public static String getOssKey(FileObject fileObject) {
        return (String) fileObject.getAttributes().get("oss-key");
    }

    public static void setOssBucket(FileObject fileObject, String bucket) {
        fileObject.getAttributes().put("oss-bucket", bucket);
    }

    public static void setOssKey(FileObject fileObject, String key) {
        fileObject.getAttributes().put("oss-key", key);
    }

}

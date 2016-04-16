package in.clouthink.daas.fss.alioss.support;

import com.aliyun.oss.model.OSSObject;
import in.clouthink.daas.fss.core.FileObject;

/**
 * Created by LiangBin on 16/4/16.
 */
public interface OssFileProvider {
    
    OSSObject getOssObject(FileObject fileObject);
}

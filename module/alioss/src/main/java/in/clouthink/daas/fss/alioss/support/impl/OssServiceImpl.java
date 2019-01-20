package in.clouthink.daas.fss.alioss.support.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import in.clouthink.daas.fss.alioss.support.OssProperties;
import in.clouthink.daas.fss.alioss.support.OssService;
import in.clouthink.daas.fss.alioss.util.FileObjectUtils;
import in.clouthink.daas.fss.domain.model.FileObject;
import in.clouthink.daas.fss.core.StoreFileRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author LiangBin & dz
 */
public class OssServiceImpl implements OssService {

    static void putUserMetadata(String prefix, String property, String value, Map<String, String> metadata) {
        if (value != null) {
            metadata.put(prefix + property, value);
        }
    }

    @Autowired
    private OssProperties ossProperties;

    @Autowired
    private OSSClient client;

    @Override
    public OssProperties getOssProperties() {
        return ossProperties;
    }

    @Override
    public OSSClient getOssClient() {
        return client;
    }

    @Override
    public OSSObject getOssObject(FileObject fileObject) {
        String bucket = FileObjectUtils.getOssBucket(fileObject);
        String key = FileObjectUtils.getOssKey(fileObject);
        return client.getObject(bucket, key);
    }

    @Override
    public String resolveBucket(StoreFileRequest request) {
        String category = (String) request.getAttributes().get("category");
        String bucket = ossProperties.getBuckets().get(category);
        if (StringUtils.isEmpty(bucket)) {
            bucket = ossProperties.getDefaultBucket();
        }
        return bucket;
    }

    @Override
    public String generateKey(StoreFileRequest request) {
        String originalFilename = request.getOriginalFilename();
        Date uploadedAt = new Date();
        StringBuilder sb = new StringBuilder();

        String filename = UUID.randomUUID().toString().replace("-", "");
        sb.append(new SimpleDateFormat("yyyy/MM/dd/").format(uploadedAt)).append(filename);

        String extName = in.clouthink.daas.fss.repackage.org.apache.commons.io.FilenameUtils.getExtension(
                originalFilename);
        if (!StringUtils.isEmpty(extName)) {
            sb.append(".").append(extName);
        }

        return sb.toString();
    }

    @Override
    public ObjectMetadata createObjectMetadata(StoreFileRequest request) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(request.getContentType());
        metadata.setUserMetadata(createUserMetadata(request));
        return metadata;
    }

    private Map<String, String> createUserMetadata(StoreFileRequest request) {
        final Map<String, String> metadata = new HashMap<String, String>();

        putUserMetadata("fss-", "originalFilename", request.getOriginalFilename(), metadata);
        putUserMetadata("fss-", "prettyFilename", request.getPrettyFilename(), metadata);
        putUserMetadata("fss-", "uploadedBy", request.getUploadedBy(), metadata);

        if (request.getAttributes() != null) {
            for (Iterator<Map.Entry<String, String>> iterator = request.getAttributes()
                                                                  .entrySet()
                                                                  .iterator(); iterator.hasNext(); ) {
                Map.Entry<String, String> entry = iterator.next();
                putUserMetadata("attrs-", entry.getKey(), (String) entry.getValue(), metadata);
            }
        }

        return metadata;
    }

}

package in.clouthink.daas.fss.util;

import in.clouthink.daas.fss.core.StoreFileRequest;
import in.clouthink.daas.fss.repackage.org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MetadataUtils {

    public static Map<String, String> buildMetadata(StoreFileRequest request) {
        return buildMetadata(request, false);
    }

    public static Map<String, String> buildMetadata(StoreFileRequest request, boolean replaceNullWithEmpty) {
        Map<String, String> metadata = new HashMap<>();

        metadata.put("fss-contentType", request.getContentType());
        if (replaceNullWithEmpty && request.getContentType() == null) {
            metadata.put("fss-contentType", "");
        }

        metadata.put("fss-originalFilename", request.getOriginalFilename());
        if (replaceNullWithEmpty && request.getOriginalFilename() == null) {
            metadata.put("fss-originalFilename", "");
        }

        metadata.put("fss-prettyFilename", request.getPrettyFilename());
        if (replaceNullWithEmpty && request.getPrettyFilename() == null) {
            metadata.put("fss-prettyFilename", "");
        }

        metadata.put("fss-uploadedBy", request.getUploadedBy());
        if (replaceNullWithEmpty && request.getUploadedBy() == null) {
            metadata.put("fss-uploadedBy", "");
        }

        metadata.put("fss-size", request.getSize() > 0 ? Long.toString(request.getSize()) : "-1");

        metadata.put("fss-uploadedAt", Long.toString(System.currentTimeMillis()));

        if (request.getAttributes() != null) {
            request.getAttributes().entrySet().forEach(key -> {
                if (request.getAttributes().get(key) != null) {
                    metadata.put("fss-attrs-" + key, request.getAttributes().get(key));
                }
            });
        }
        return metadata;
    }

    public static String generateKey(StoreFileRequest request) {
        return generateKey(request, false);
    }

    public static String generateKey(StoreFileRequest request, boolean generatePath) {
        String originalFilename = request.getOriginalFilename();
        Date uploadedAt = new Date();
        StringBuilder sb = new StringBuilder();

        String filenameWithoutSuffix = UUID.randomUUID().toString().replace("-", "");
        if (generatePath) {
            sb.append(new SimpleDateFormat("yyyy/MM/dd/").format(uploadedAt));
        }
        sb.append(filenameWithoutSuffix);

        String extName = FilenameUtils.getExtension(originalFilename);
        if (!StringUtils.isEmpty(extName)) {
            sb.append(".").append(extName);
        }

        return sb.toString();
    }

}

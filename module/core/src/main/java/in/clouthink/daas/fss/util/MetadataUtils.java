package in.clouthink.daas.fss.util;

import in.clouthink.daas.fss.core.StoreFileRequest;

import java.util.HashMap;
import java.util.Map;

public class MetadataUtils {

    public static Map<String, String> buildMetadata(StoreFileRequest request) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("fss-contentType", request.getContentType());
        metadata.put("fss-originalFilename", request.getOriginalFilename());
        metadata.put("fss-prettyFilename", request.getPrettyFilename());
        metadata.put("fss-uploadedBy", request.getUploadedBy());
        metadata.put("fss-size", request.getSize() > 0 ? Long.toString(request.getSize()) : null);
        metadata.put("fss-uploadedAt", Long.toString(System.currentTimeMillis()));
        if (request.getAttributes() != null) {
            request.getAttributes().entrySet().forEach(key -> {
                metadata.put("fss-attrs-" + key, request.getAttributes().get(key));
            });
        }
        return metadata;
    }

}

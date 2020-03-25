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

        metadata.put("fss-contenttype", request.getContentType());
        if (replaceNullWithEmpty && request.getContentType() == null) {
            metadata.put("fss-contenttype", "");
        }

        metadata.put("fss-originalfilename", request.getOriginalFilename());
        if (replaceNullWithEmpty && request.getOriginalFilename() == null) {
            metadata.put("fss-originalfilename", "");
        }

        metadata.put("fss-prettyfilename", request.getPrettyFilename());
        if (replaceNullWithEmpty && request.getPrettyFilename() == null) {
            metadata.put("fss-prettyfilename", "");
        }

        metadata.put("fss-uploadedby", request.getUploadedBy());
        if (replaceNullWithEmpty && request.getUploadedBy() == null) {
            metadata.put("fss-uploadedby", "");
        }

        metadata.put("fss-size", request.getSize() > 0 ? Long.toString(request.getSize()) : "-1");

        metadata.put("fss-uploadedat", Long.toString(System.currentTimeMillis()));

        if (request.getAttributes() != null) {
            request.getAttributes().entrySet().forEach(key -> {
                if (request.getAttributes().get(key) != null) {
                    metadata.put("fss-attrs-" + key, request.getAttributes().get(key));
                }
            });
        }
        return metadata;
    }

    /**
     * @param request
     * @return filename without '/' prefix
     */
    public static String generateFilename(StoreFileRequest request) {
        return generateFilename(request, false);
    }

    /**
     * @param request
     * @param generatePath
     * @return filename without '/' prefix
     */
    public static String generateFilename(StoreFileRequest request, boolean generatePath) {
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

    /**
     * @param request
     * @param path
     * @return filename without '/' prefix
     */
    public static String generateFilename(StoreFileRequest request, String path) {
        String originalFilename = request.getOriginalFilename();
        StringBuilder sb = new StringBuilder();

        String filenameWithoutSuffix = UUID.randomUUID().toString().replace("-", "");
        if (!StringUtils.isEmpty(path)) {
            sb.append(path);
            if (!path.endsWith("/")) {
                sb.append("/");
            }
        }
        sb.append(filenameWithoutSuffix);

        String extName = FilenameUtils.getExtension(originalFilename);
        if (!StringUtils.isEmpty(extName)) {
            sb.append(".").append(extName);
        }

        return sb.toString();
    }

}

package in.clouthink.daas.fss.alioss.builder;

import in.clouthink.daas.fss.alioss.model.OssFileObject;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import java.util.UUID;

/**
 * Created by LiangBin on 16/4/17.
 */
public class OssFileObjectBuilder extends
                                  AbstractBuilder<OssFileObject, OssFileObjectBuilder> {
                                  
    public static OssFileObjectBuilder create() {
        return create(OssFileObjectBuilder.class);
    }
    
    @Override
    public OssFileObjectBuilder valid() {
        return super.valid().withId()
                            .withContentType()
                            .withOriginalFilename()
                            .withUploadedBy();
    }
    
    public OssFileObjectBuilder withId(String id) {
        d.setId(id);
        return this;
    }
    
    public OssFileObjectBuilder withId() {
        String id = UUID.randomUUID().toString().replace("-", "") + ".txt";
        return withId(id);
    }
    
    public OssFileObjectBuilder withContentType(String contentType) {
        d.setContentType(contentType);
        return this;
    }
    
    public OssFileObjectBuilder withContentType() {
        return withContentType("text/plain");
    }
    
    public OssFileObjectBuilder withOriginalFilename(String originalFilename) {
        d.setOriginalFilename(originalFilename);
        return this;
    }
    
    public OssFileObjectBuilder withOriginalFilename() {
        String filename = RandomStringUtils.randomAlphanumeric(8) + ".txt";
        return withOriginalFilename(filename);
    }
    
    public OssFileObjectBuilder withUploadedBy(String uploadedBy) {
        d.setUploadedBy(uploadedBy);
        return this;
    }
    
    public OssFileObjectBuilder withUploadedBy() {
        return withUploadedBy(RandomStringUtils.randomAlphanumeric(5));
    }
}

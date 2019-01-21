package in.clouthink.daas.fss.zimg.impl;

import in.clouthink.daas.fss.zimg.client.ZimgError;
import in.clouthink.daas.fss.zimg.client.ZimgResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.InputStream;

@Component
public class ZimgClient {

    private static final Log logger = LogFactory.getLog(ZimgClient.class);

    private RestTemplate restTemplate = new RestTemplate();

    public ZimgResult upload(InputStream inputStream, String uploadEndpoint) {
        HttpHeaders headers = new HttpHeaders();

        InputStreamResource resource = new InputStreamResource(inputStream);
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("fileImg", resource);

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(param,
                                                                                                             headers);

        return doUpload(httpEntity, uploadEndpoint);
    }

    public ZimgResult upload(File file, String uploadEndpoint) {
        HttpHeaders headers = new HttpHeaders();

        FileSystemResource resource = new FileSystemResource(file);
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("userfile", resource);

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(param,
                                                                                                             headers);

        return doUpload(httpEntity, uploadEndpoint);
    }

    public ZimgResult doUpload(HttpEntity<MultiValueMap<String, Object>> httpEntity, String uploadEndpoint) {
        try {
            ResponseEntity<ZimgResult> responseEntity = restTemplate.exchange(uploadEndpoint,
                                                                              HttpMethod.POST,
                                                                              httpEntity,
                                                                              ZimgResult.class);

            logger.info(String.format("POST to %s and the status code is %s",
                                      uploadEndpoint,
                                      responseEntity.getStatusCode()));

            return responseEntity.getBody();
        } catch (Throwable e) {
            logger.error(e, e);
            return new ZimgResult(false, new ZimgError(500, e.getMessage()));
        }
    }

    public void delete(String filename, String downloadEndpoint) {

    }
}

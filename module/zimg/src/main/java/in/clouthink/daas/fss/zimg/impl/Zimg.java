package in.clouthink.daas.fss.zimg.impl;

import org.springframework.web.client.RestTemplate;

public class Zimg {

    private String downloadUrl;

    private RestTemplate restTemplate ;

    public Zimg() {
    }

    public Zimg(String downloadUrl, RestTemplate restTemplate) {
        this.downloadUrl = downloadUrl;
        this.restTemplate = restTemplate;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}

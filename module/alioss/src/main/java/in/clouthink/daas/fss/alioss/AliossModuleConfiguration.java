package in.clouthink.daas.fss.alioss;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aliyun.oss.OSSClient;

import in.clouthink.daas.fss.alioss.impl.DefaultOssFileProvider;
import in.clouthink.daas.fss.alioss.impl.DefaultOssStrategy;
import in.clouthink.daas.fss.alioss.impl.FileObjectServiceImpl;
import in.clouthink.daas.fss.alioss.impl.FileStorageServiceImpl;
import in.clouthink.daas.fss.alioss.support.OssConfig;
import in.clouthink.daas.fss.alioss.support.OssFileProvider;
import in.clouthink.daas.fss.alioss.support.OssStrategy;
import in.clouthink.daas.fss.spi.FileObjectService;
import in.clouthink.daas.fss.spi.FileStorageService;

/**
 * Created by dz on 16/3/29.
 */
@Configuration
public class AliossModuleConfiguration {
    
    @Resource
    private OssConfig ossConfig;
    
    @Bean
    public OSSClient ossClient() {
        return new OSSClient(ossConfig.getEndpoint(),
                             ossConfig.getKeyId(),
                             ossConfig.getSecret());
    }
    
    @Bean
    public OssStrategy ossStrategy() {
        return new DefaultOssStrategy();
    }

    @Bean
    public OssFileProvider ossFileProvider() {
        return new DefaultOssFileProvider();
    }
    
    @Bean
    public FileObjectService fileObjectService() {
        return new FileObjectServiceImpl();
    }
    
    @Bean
    public FileStorageService fileStorageService() {
        return new FileStorageServiceImpl();
    }
    
}

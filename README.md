# Introduction

The fss(for file storage service) apis make storing the blob file easy and simple , 
the user can supply the provider impl to match the customize requirement if the default build-in impl is not enough.

# Dependencies

# Latest Release

So far the following version is available 

module name | latest version
------|------
daas-fss-core | 2.0.0
daas-fss-mongodb| 2.0.0
daas-fss-alioss| 2.0.0

## Maven

    <dependency>
        <groupId>in.clouthink.daas</groupId>
        <artifactId>daas-fss-core</artifactId>
        <version>${daas.fss.core.version}</version>
    </dependency>

    <dependency>
        <groupId>in.clouthink.daas</groupId>
        <artifactId>daas-fss-mongodb</artifactId>
        <version>${daas.fss.mongodb.version}</version>
    </dependency>
    
    <dependency>
        <groupId>in.clouthink.daas</groupId>
        <artifactId>daas-fss-alioss</artifactId>
        <version>${daas.fss.alioss.version}</version>
    </dependency>
    
    <dependency>
        <groupId>in.clouthink.daas</groupId>
        <artifactId>daas-edm</artifactId>
        <version>1.0.1</version>
    </dependency>

## Gradle

    compile "in.clouthink.daas:daas-fss-core:${daas_fss_core_version}"
    compile "in.clouthink.daas:daas-fss-mongodb:${daas_fss_mongodb_version}"
    compile "in.clouthink.daas:daas-fss-alioss:${daas_fss_alioss_version}"
    compile "in.clouthink.daas:daas-edm:1.0.1"

# Alioss configuration for example

## application.properties

Please populate the property value from the alioss you configured 

    fss.sample.alioss.keyId=<The alioss access key id>
    fss.sample.alioss.secret=<The alioss access secret>
    fss.sample.alioss.ossDomain=oss-cn-shenzhen.aliyuncs.com
    fss.sample.alioss.imgDomain=img-cn-shenzhen.aliyuncs.com
    fss.sample.alioss.defaultBucket=<The alioss default bucket name>
    fss.sample.alioss.buckets.<category>=<The alioss bucket name for the category>

## Spring Boot Application

    @ConfigurationProperties(prefix = "fss.sample.alioss")
    public class AliossSampleProperties extends DefaultOssProperties {
    }
    
    @Configuration
    @EnableConfigurationProperties(AliossSampleProperties.class)
    @ComponentScan({"in.clouthink.daas.fss.mongodb.model"})
    @EnableMongoRepositories({"in.clouthink.daas.fss.mongodb.repository"})
    public class FssConfiguration {
    
    	@Bean
    	@Autowired
    	public OSSClient ossClient(AliossSampleProperties aliossSampleProperties) {
    		return new OSSClient("http://" + aliossSampleProperties.getOssDomain(),
    							 aliossSampleProperties.getKeyId(),
    							 aliossSampleProperties.getSecret());
    	}
    
    	@Bean
    	public OssService ossStrategy() {
    		return new OssServiceImpl();
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

# Gridfs configuration for example

`TODO`

# Appendix - SPI

`TODO`

# Introduction

The fss(for file storage service) apis make storing the blob file easy and simple , 
the user can supply the provider impl to match the customize requirement if the default build-in impl is not enough.

# Dependencies

# Latest Release

So far the following version is available 

|category  |module name | latest version |
|---|---|---|
| core | daas-fss-core | [3.0.1](https://mvnrepository.com/artifact/in.clouthink.daas/daas-fss-core/3.0.1)
| physical file | daas-fss-zimg | [3.0.1](https://mvnrepository.com/artifact/in.clouthink.daas/daas-fss-zimg/3.0.1)
| | daas-fss-gridfs | [3.0.1](https://mvnrepository.com/artifact/in.clouthink.daas/daas-fss-gridfs/3.0.1)
| | daas-fss-alioss | [3.0.1](https://mvnrepository.com/artifact/in.clouthink.daas/daas-fss-alioss/3.0.1)
| | daas-fss-s3 | [3.0.1](https://mvnrepository.com/artifact/in.clouthink.daas/daas-fss-alioss/3.0.1)
| | daas-fss-webdev | [3.0.1](https://mvnrepository.com/artifact/in.clouthink.daas/daas-fss-webdav/3.0.1)
| | daas-fss-glusterfs | [3.0.1](https://mvnrepository.com/artifact/in.clouthink.daas/daas-fss-glusterfs/3.0.1)
| | daas-fss-qiniu | [3.0.1](https://mvnrepository.com/artifact/in.clouthink.daas/daas-fss-qiniu/3.0.1)
| database | daas-fss-mysql | [3.0.1](https://mvnrepository.com/artifact/in.clouthink.daas/daas-fss-mysql/3.0.1)
| |daas-fss-mongodb | [3.0.1](https://mvnrepository.com/artifact/in.clouthink.daas/daas-fss-mongodb/3.0.1)

> Now 2.0.0 is deprecated.  

# Usage 

## Spring Mvc

```java

```


## Zimg


### Quick Start

Here is the sample code using in your application most frequently. 

```java


```

Now let's make an explain how to make it running.

### Prepare zimg server

Let's quick start it by docker-compose 

```yml



```

```bash

```

### Spring boot it

Add new Java class named FssConfiguration annotated with `@Configuration` and create the `@Bean` 

* FilePipe
* FileStoreService
* FileStoreRepository
* ZimgClient

Here is the sample:

```java

```

### Zimg client Properties Configuration


application.yml

```yml

```

application.properties

```properties

```





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

# Mongodb GridFS configuration for example

`TODO`

# Appendix - Build the source

Install all (skip test)

```shell
mvn clean install -Dmaven.test.skip=true
```

Build all

```shell
mvn clean package
```

Build single project

* core

```shell
mvn -pl module/core clean package
# or
mvn --projects module/core clean package
```

* zimg

```shell
mvn -pl module/zimg lean package -am
# or
mvn --projects module/zimg clean package --also-make
```

* alioss

```shell
mvn -pl module/alioss clean package -am
# or
mvn --projects module/alioss clean package --also-make
```

* mongodb

```shell
mvn -pl module/mongodb clean package -am
# or
mvn --projects module/mongodb clean package --also-make
```

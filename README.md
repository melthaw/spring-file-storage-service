# Introduction

The fss(for file storage service) apis make storing the blob file easy and simple , 
the user can supply the provider impl to match the customize requirement if the default build-in impl is not enough.

# Dependencies

# Latest Release

So far the following version is available 

module name | latest version
------|------
daas-fss-core | 3.0.0
daas-fss-zimg| 3.0.0
daas-fss-mongodb| 3.0.0
daas-fss-alioss| 3.0.0

> Now 2.0.0 is deprecated.  

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
        <artifactId>daas-fss-zimg</artifactId>
        <version>${daas.fss.zimg.version}</version>
    </dependency>
    
## Gradle

    compile "in.clouthink.daas:daas-fss-core:${daas_fss_core_version}"
    compile "in.clouthink.daas:daas-fss-zimg:${daas_fss_zimg_version}"
    compile "in.clouthink.daas:daas-fss-mongodb:${daas_fss_mongodb_version}"
    compile "in.clouthink.daas:daas-fss-alioss:${daas_fss_alioss_version}"


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

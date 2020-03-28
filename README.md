# Introduction

The fss(for file storage service) apis make storing the blob file easy and simple , 
the user can supply the provider impl to match the customize requirement if the default build-in impl is not enough.

Here is the support list:

* Alioss
* Qiniu
* Amazon S3 Protocol
* WebDav Protocol
* Glusterfs
* Gridfs
* Zimg
* Local File

# Dependencies

# Latest Release

So far the following version is available 

|category  |module name | latest version |
|---|---|---|
| core | daas-fss-core | [3.0.6](https://mvnrepository.com/artifact/in.clouthink.daas/daas-fss-core/3.0.6)
| physical file | daas-fss-zimg | [3.0.6](https://mvnrepository.com/artifact/in.clouthink.daas/daas-fss-zimg/3.0.6)
| | daas-fss-gridfs | [3.0.6](https://mvnrepository.com/artifact/in.clouthink.daas/daas-fss-gridfs/3.0.6)
| | daas-fss-alioss | [3.0.6](https://mvnrepository.com/artifact/in.clouthink.daas/daas-fss-alioss/3.0.6)
| | daas-fss-s3 | [3.0.6](https://mvnrepository.com/artifact/in.clouthink.daas/daas-fss-alioss/3.0.6)
| | daas-fss-webdev | [3.0.6](https://mvnrepository.com/artifact/in.clouthink.daas/daas-fss-webdav/3.0.6)
| | daas-fss-glusterfs | [3.0.6](https://mvnrepository.com/artifact/in.clouthink.daas/daas-fss-glusterfs/3.0.6)
| | daas-fss-qiniu | [3.0.6](https://mvnrepository.com/artifact/in.clouthink.daas/daas-fss-qiniu/3.0.6)
| | daas-fss-local | [3.0.6](https://mvnrepository.com/artifact/in.clouthink.daas/daas-fss-local/3.0.6)
| database | daas-fss-mysql | [3.0.6](https://mvnrepository.com/artifact/in.clouthink.daas/daas-fss-mysql/3.0.6)
| |daas-fss-mongodb | [3.0.6](https://mvnrepository.com/artifact/in.clouthink.daas/daas-fss-mongodb/3.0.6)

> And 2.0.0 is deprecated.  

# Get Started 

We will take [WebDAV](http://www.webdav.org/) for example. 

* Start a file server supporting WebDAV protocol
* Gradle as build tool
* Show the API usage in Spring Application 

## Start WebDav server

> please make sure docker & docker-compose are ready on your local machine.

We will quick start a WebDAV file server by docker-compose.

Here is the `docker-compose.yml`

```yml
version: '3'
services:

  webdav:
    container_name: fss-test-webdav
    image: bytemark/webdav
    ports:
      - "80:80"
    environment:
      AUTH_TYPE: Digest
      USERNAME: alice
      PASSWORD: secret1234
```

Start it with

```sh
docker-compose up -d
```

Now `80` port is exported as hosted port 

## Introduce to your project

Gradle build.gradle

```gradle
    compile("in.clouthink.daas:daas-fss-core:3.0.6")
    compile("in.clouthink.daas:daas-fss-webdav:3.0.6")
```

Maven pom.xml 

```xml

	<dependency>
		<groupId>in.clouthink.daas</groupId>
		<artifactId>daas-fss-core</artifactId>
		<version>3.0.6</version>
	</dependency>
        
	<dependency>
		<groupId>in.clouthink.daas</groupId>
		<artifactId>daas-fss-webdav</artifactId>
		<version>3.0.6</version>
	</dependency>

```

## Spring 

Application

```java
@SpringBootApplication
@Import({WebDavAutoConfiguration.class})
@EnableConfigurationProperties(WebDavApplication.TestWebDavProperties.class)
public class WebDavApplication {

    @ConfigurationProperties(prefix = "fss.webdav")
    public static class TestWebDavProperties extends DefaultWebDavProperties {

    }

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WebDavApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(new Object[]{WebDavApplication.class}, args);
    }

}


```

Spring `application.yml`

```yml
fss:
  webdav:
    username: alice
    password: secret1234
    endpoint: http://127.0.0.1

logging:
  level:
    in.clouthink: debug
    com.github.sardine: debug
```

Then let's create a test 

```java
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebDavApplication.class)
public class WebDavTest {

    @Resource
    @Qualifier("webdavStorage")
    private FileStorage fileStorage;

    private ClassPathResource pdfResource = new ClassPathResource("please_replace_with_your_test_file.pdf");

    @Test
    public void test() throws IOException {
        Assert.assertTrue(pdfResource.exists());

        DefaultStoreFileRequest request = new DefaultStoreFileRequest();
        request.setOriginalFilename(pdfResource.getFilename());
        request.setContentType(MediaType.APPLICATION_PDF.toString());
        StoreFileResponse response = fileStorage.store(pdfResource.getInputStream(), request);

        Assert.assertEquals("webdav", response.getProviderName());

        StoredFileObject storedFileObject = response.getStoredFileObject();
        Assert.assertNotNull(storedFileObject);

        storedFileObject = fileStorage.findByStoredFilename(storedFileObject.getStoredFilename());
        String saveToFilename = MetadataUtils.generateFilename(request);
        storedFileObject.writeTo(new FileOutputStream(saveToFilename), 1024 * 4);

        storedFileObject = fileStorage.delete(storedFileObject.getStoredFilename());
        Assert.assertNull(storedFileObject.getImplementation());
    }

}
```

# Configuration Reference

Default implementation is build inside , here is the sample to quick enable it in Spring. 

```java
@EnableConfigurationProperties(WebDavApplication.TestOssProperties.class)
public class WebDavApplication {

    @ConfigurationProperties(prefix = "fss.alioss")
    public static class TestOssProperties extends DefaultOssProperties {

    }
    
    ...
}

```


## Alioss

Definition

> `in.clouthink.daas.fss.alioss.support.OssProperties`

Implementation 

> `in.clouthink.daas.fss.alioss.support.DefaultOssProperties`

Sample

```yml
fss:
  alioss:
    keyId: <your alioss key id>
    keySecret: <your alioss key secret>
    endpoint: oss-cn-shenzhen.aliyuncs.com
    defaultBucket: testfss
    clientConfiguration:
      socketTimeout: 5000
      connectionTimeout: 5000
```

## FastDfs


Definition

> `in.clouthink.daas.fss.fastdfs.support.FastdfsProperties`

Implementation 

> `in.clouthink.daas.fss.fastdfs.support.DefaultFastdfsProperties`

Sample

```yml
fss:
  fastdfs:
    connectTimeoutInseconds: 30
    networkTimeoutInSeconds: 60
    charset: UTF-8
    httpAntiStealToken: false
    httpSecretKey:
    httpTrackerHttpPort: 8080
    trackerServers:
      - tracker:22122
```



## Glusterfs

Definition

> `in.clouthink.daas.fss.glusterfs.support.GlusterfsProperties`

Implementation 

> `in.clouthink.daas.fss.glusterfs.support.DefaultGlusterfsProperties`

Sample

```yml
fss:
  glusterfs:
    server: glusterfs1
    volume: test-volume
```


## Gridfs

> Share the Spring Mongodb Data configuration and no more special.

```yml
spring:
  data:
    mongodb:
      uri: mongodb://${MONGODB_HOST:localhost}:${MONGODB_PORT:27017}/${MONGODB_DB:daas-fss}
```

## Qiniu Cloud


Definition

> `in.clouthink.daas.fss.qiniu.support.QiniuProperties`

Implementation 

> `in.clouthink.daas.fss.qiniu.support.DefaultQiniuProperties`

Sample

```yml
fss:
  qiniu:
    accessKey: <your qiniu access key>
    secretKey: <your qiniu secret ky>
    host: <your qiniu subdomain>.bkt.clouddn.com
    defaultBucket: testfss
```



## Amazon S3 Protocol 


Definition

> `in.clouthink.daas.fss.s3.support.S3Properties`

Implementation 

> `in.clouthink.daas.fss.s3.support.DefaultS3Properties`

Sample

```yml
fss:
  s3:
    accessKey: <your s3 access key>
    secretKey: <your s3 secret key>
    endpoint: <your s3 endpoint>
    region: us-west-2
    bucketStyle: path
    defaultBucket: test
    clientConfiguration:
      connectionTimeout: 5000
```


## WebDAV 

Definition

> `in.clouthink.daas.fss.webdav.support.WebdavProperties`

Implementation 

> `in.clouthink.daas.fss.webdav.support.DefaultWebdavProperties`

Sample

```yml
fss:
  webdav:
    username: alice
    password: secret1234
    endpoint: http://127.0.0.1
```

## Zimg

Definition

> `in.clouthink.daas.fss.zimg.support.ZimgProperties`

Implementation 

> `in.clouthink.daas.fss.zimg.support.DefaultZimgProperties`

Sample

```yml
fss:
  zimg:
    uploadEndpoint: http://127.0.0.1:4869/upload
    downloadEndpoint: http://127.0.0.1:4869
    adminEndpoint: http://127.0.0.1:4869/admin
    infoEndpoint: http://127.0.0.1:4869/info

```


## Local File


Definition

> `in.clouthink.daas.fss.local.support.LocalFileProperties`

Implementation 

> `in.clouthink.daas.fss.local.support.DefaultLocalFileProperties`

Sample

```yml
fss:
  local:
    store-path: <your local file store path>
```


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

* alioss

```shell
mvn -pl module/alioss clean package -am
# or
mvn --projects module/alioss clean package --also-make
```

* fastdfs

```shell
mvn -pl module/fastdfs lean package -am
# or
mvn --projects module/fastdfs clean package --also-make
```

* glusterfs

```shell
mvn -pl module/glusterfs lean package -am
# or
mvn --projects module/glusterfs clean package --also-make
```

* gridfs

```shell
mvn -pl module/gridfs lean package -am
# or
mvn --projects module/gridfs clean package --also-make
```

* qiniu

```shell
mvn -pl module/qiniu lean package -am
# or
mvn --projects module/qiniu clean package --also-make
```

* s3

```shell
mvn -pl module/s3 lean package -am
# or
mvn --projects module/s3 clean package --also-make
```

* webdav

```shell
mvn -pl module/webdav lean package -am
# or
mvn --projects module/webdav clean package --also-make
```

* zimg

```shell
mvn -pl module/zimg lean package -am
# or
mvn --projects module/zimg clean package --also-make
```

* local

```shell
mvn -pl module/local lean package -am
# or
mvn --projects module/local clean package --also-make
```

* mongodb

```shell
mvn -pl module/mongodb clean package -am
# or
mvn --projects module/mongodb clean package --also-make
```

* mysql

```shell
mvn -pl module/mysql lean package -am
# or
mvn --projects module/mysql clean package --also-make
```

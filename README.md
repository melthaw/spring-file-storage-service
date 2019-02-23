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

> And 2.0.0 is deprecated.  

# Get Started 

We will take fastdfs for example. 

* Start file server support webdav protocol
* Gradle as build tool
* Show the API usage in Spring Application 

## Start fastdfs server

We will quick start a webdav file server by docker-compose , please make sure docker & docker-compose are prepare on your local machine.

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
    compile("in.clouthink.daas:daas-fss-core:3.0.1")
    compile("in.clouthink.daas:daas-fss-webdav:3.0.1")
```


Maven pom.xml 

```xml

	<dependency>
		<groupId>in.clouthink.daas</groupId>
		<artifactId>daas-fss-core</artifactId>
		<version>3.0.1</version>
	</dependency>
        
	<dependency>
		<groupId>in.clouthink.daas</groupId>
		<artifactId>daas-fss-webdav</artifactId>
		<version>3.0.1</version>
	</dependency>

```


## Spring 

Application

```java
@SpringBootApplication
@Import({WebDavAutoConfiguration.class})
@EnableConfigurationProperties(WebDavApplication.TestOssProperties.class)
public class WebDavApplication {

    @ConfigurationProperties(prefix = "fss.webdav")
    public static class TestOssProperties extends DefaultWebDavProperties {

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

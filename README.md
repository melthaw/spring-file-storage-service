# Introduction

The fss(for file storage service) apis make storing the blob file easy and simple , 
the user can supply the provider impl to match the customize requirement if the default build-in impl is not enough.

# Dependencies


# Usage

So far the following version is available 

module name | latest version
------|------
daas-fss-core |1.0.0
daas-fss-mongodb|1.0.0
daas-fss-gridfs|1.0.0
daas-fss-alioss |1.0.0
daas-fss-rest |1.0.0
daas-fss-sample |1.0.0


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
        <artifactId>daas-fss-gridfs</artifactId>
        <version>${daas.fss.gridfs.version}</version>
    </dependency>
    
    <dependency>
        <groupId>in.clouthink.daas</groupId>
        <artifactId>daas-fss-rest</artifactId>
        <version>${daas.fss.rest.version}</version>
    </dependency>

## Gradle

    compile "in.clouthink.daas:daas-fss-core:${daas_fss_core_version}"
    compile "in.clouthink.daas:daas-fss-mongodb:${daas_fss_mongodb_version}"
    compile "in.clouthink.daas:daas-fss-gridfs:${daas_fss_gridfs_version}"
    compile "in.clouthink.daas:daas-fss-rest:${daas_fss_rest_version}"


# For Example

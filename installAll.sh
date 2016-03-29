#!/bin/sh
mvn -f module/core/pom.xml clean install
mvn -f module/mongodb/pom.xml clean install
mvn -f module/gridfs/pom.xml clean install
mvn -f module/alioss/pom.xml clean install
mvn -f module/rest/pom.xml clean install

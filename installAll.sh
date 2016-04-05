#!/bin/sh
mvn -f module/core/pom.xml clean install
mvn -f module/mongodb/pom.xml clean install
#mvn -f module/sample/pom.xml clean install

# ADML


The project can be built using Maven, and the resulting .war file can be dropped into Tomcat (or just launched with `java -jar`).
The stack is Spring Boot/Spring MVC/Spring Data.


Installation
--------------


- Install [Maven](https://maven.apache.org/) 
(and [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)).

* Build and Launch

```sh

# from the folder, run the build, and package it:

mvn clean install

# to test it:

java -jar target/adml-0.0.1-SNAPSHOT.war

```


Try it
--------------
- Hit (for local testing): http://localhost:8080/
- For local Tomcat: http://localhost:8080/adml

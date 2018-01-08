# Archives Digital Media Log

This webapp enables accessioning or processing archivists to create a log of media
items within archival collections. This tool will facilitate keeping track of media
that is in specific collection boxes/containers in aggregate terms per media format
type. The output from this tool will enhance the collection management records in
ArchivesSpace without needing to manually type that information again.

The stack is Spring Boot/MVC/Security/Data. The tool uses an embedded database (h2) in development mode.


Dependencies
-------------

- A running ArchivesSpace instance
- Maven for building the project.
- connection.props to specify aspace and app user credentials
- A relational database (for production use)

When deploying to production:

- Supply application-prod.properties to specify database credentials. This will be replaced
with Tomcat JNDI connections in future.
- Grep for IPs for ASpace and production machine IP when deploying in production and change
the IPs from "localhost" to the new address.


Build
--------------

The project can be built using Apache Maven, and the resulting .war file can just be dropped into Tomcat 
(or just launched with `java -jar`).

```sh

# from the folder, run the build, and package it:

mvn clean install -P dev

# to test it:

java -jar target/adml-0.0.1-SNAPSHOT.war

```

Test
--------

Visit http://localhost:8080/adml.


Server Deployment
-------------------
- Copy application-prod.properties and connection.properties to resources directory.
- Compile the application with the -P prod flag (not -P dev)
- Install MySql
- Install httpd and enable reverse proxy.
- Install Tomcat.
- Drop the .war to webapps folder.

Where is this service deployed?
---------------------------------
- In iasc.mit.edu slash adml.
- You can find the war file in Tomcat /opt/...
- The app lives on a Digital Ocean Cent OS droplet.

Backups
-------

The system is snapshot on a regular basis. (In future, a process will be devised to
create database dumps and export them to an external system.)


Users
------

- IASC, MIT

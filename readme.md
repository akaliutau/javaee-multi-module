
In brief 
============

This is a multi-module project implementing a REST Data server and web interface for it.


Overview
===========
The multi-module project developed here consists of 9 modules – 1 parent, 1 transitive parent and 7 submodules.

A parent project with <pom> packaging does not have any resources, it is just a build file that refers to other Maven projects.

Seven submodules of the parent module are:

model

This module defines a simple object model which models the data returned from/sent to the dataserver.
This project contains model objects annotated with javax.validation.constraints Annotations. These annotations are used by the logic in webclient to validate input data.

dao

This module contains some Data Access Objects (DAO) which are configured to store employee and department records in the database. This module has a direct dependency on  model (model objects  are used in process of  forming the SQL queries). The Spring Framework JDBC abstraction is chosen for JDBC database access.

service

This module defines the service objects which are referenced by dataserver. The sevice layer can be viewed as set of wrapper classes for DAO and introduced in order to separate dao and web layers.

endpoints

This is a static module which is used as URI store and referenced by both the dataserver and dataclient modules. There are 11 end points in total.

dataserver

This module contains one REST Controller implementation which uses the services defined in service module and the DAOs defined in dao module. 

REST web service is chosen because of its flexibility and easiness, simplicity of integration into Spring's MVC layer. Rest service in current implementation uses  a very lightweight and human-read JSON format in request/response.

dataclient

This module contains a logic to communicate with remote dataserver and to hold retrieved data. Consequently, this module depends on the remote business interfaces from the dataserver component. 

webclient

This module contains four Spring MVC Controller implementations which use the service defined in dataclient module to retrieve/send data. Client  uses  a popular JavaScript library JQuiery in web layer. All input data is verified against multiply constraints.



For logging support the log4j framework is chosen.

For testing JUnit, Spring-test and Mockito libraries are used (the latter for so called mock object techniques). Tests cover major classes in all modules. DAO layer is tested with the help of embedded database H2.

All classes have the comments in Javadoc style.

System Requirements 
=====================
In order to  compile and deploy these applications the following software is needed.

* Maven 3.x

* Java JDK 1.7+


For starting a DataServer:

* Java JRE 1.7+

* Tomcat 7.x, 8.x (application was tested on Tomcat 8.0.36)

* MySQL 5.x as primary database.

For starting a Webclient application:

* Java JRE 1.7+

* Tomcat 7.x, 8.x


Building project
========================

To build all modules, use the following maven command from the top project root directory:

mvn clean install


Installing dataserver
========================
DataServer is working in tie-up with MySQL. 
Configuration of mysql service is done via  file db.properties  in multi-module/dataserver/src/main/resources/db directory. 

Sample database orgdb can be instantiated with the help of SQL script (can be found in the same directory):

mysql -u username -p < createdb.sql

You must provide the necessary credentials – username and password.

By default mysql service is available at lockalhost:3306

NB: Mysql daemon must be run  and database orgdb must be created before running DataServer.


To deploy a dataserver application to Tomcat 8.x, you may use the following  maven command from the multi-module/dataserver project root directory (Tomcat container must be online): 

mvn cargo:deploy

You must provide the necessary  credentials in the parent POM.xml file in multi-module project root directory, see the <pluginManagement> section.

Alternatively,  you should  just copy the dataserver.war  file to CATALINA_HOME/webapps directory (of course, you need the appropriate access rights).

Alternatively,  you should use the Tomcat Manager web application interactively (via HTML GUI) to deploy/redeploy/undeploy  a new web application from the uploaded contents of a WAR file.

Rest service is available at the following end points (base URI is omitted):
===============================================================
No.	URI	HTTP Method	Parameters (Mandatory, Optional)
===============================================================
0	/empl	GET			( - , id)

1	/empl/get	GET			( - , id)

2	/empl/getlist	GET			(-,f,n, id)

3	/empl/update	POST			(-, new )

4	/empl/delete	GET			(id, - )

5	/empl/search	GET			(-,-)

6	/dep	GET			( - , -)

7	/dep/get	GET			( - , id)

8	/dep/getlist	GET			(-,f, n)

9	/dep/update	POST			(-, new )

10	/dep/delete	GET			(id, - )



To test service availability, hit in any browser
[ServerHostURI]/[ServerBaseURI]/dep/getlist

This uri defines a location of the list of departments from database.

With default configuration the last address will look like

http://localhost:8080/dataserver/dep/getlist



Installing WebClient
====================

Configuration of data service for client side is done via file server.properties in the multi-module/webclient/src/main/resources directory. There are only two parameters to edit - ServerBaseURI and ServerHostURI.
 ServerBaseURI is depended  on the  name under which application will be deployed to Tomcat. Default values are the following:

ServerHostURI=http://localhost:8080

ServerBaseURI=/dataserver

The process of deploying a webclient application is  the same as dataserver - type the following maven command in console from the multi-module/webclient project root directory:

mvn cargo:deploy

or copy the webclient.war file to CATALINA_HOME/webapps directory.

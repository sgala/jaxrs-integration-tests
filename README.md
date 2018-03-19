# JAXRS integration test suite

###To run execute maven test goal:
`mvn test` or `mvn test -P arq-tomcat-resteasy` - test Resteasy implementation

`mvn test -P arq-tomcat-cxf` - test CXF implementation (does not work yet)

###How it works:
Uses Arquillian with Tomcat managed instance. There are preconfigured bundled server archives for each of the implementations.
When the maven test goal is run the appropriate server is unpacked into target directory and started.
Arquillian creates a separate WAR file for each test group, deploys it to the server and executes the tests.

###High level structure:
*src/test/java/com/readhat*: contains all the tests, there is a package for each part of JAXRS specification.
There is a resources package for each one which contains utils and WAR file configurations.

*src/test/java/com/readhat/utils*: contains util classes used by the whole test suite

*src/test/java/resources/com/redhat*: various HTML and XML files needed by the test suite

*src/test/java/resources/arquillian.xml*: tomcat config file

*apache-tomcat-resteasy.zip*: preconfigured tomcat with resteasy

*apache-tomcat-cxf.zip*: preconfigured tomcat with CXF (not available yet)







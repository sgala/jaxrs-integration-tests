# JAXRS integration test suite

##To run execute maven test goal:

Set up the following environmental variables:  
export JAVA_HOME=/etc/alternatives/java_sdk_1.8.0/  
export PATH=$JAVA_HOME/bin:/opt/apache-maven-3.3.9/bin:$PATH  
export HUDSON_STATIC_ENV=/qa/services/hudson/STATIC/  
export WORKSPACE_BASEDIR=/tmp/jaxrs-integration-test  

Then user maven tu run the tests:  
`mvn test -P arq-jws-server-cxf` - test Resteasy implementation  
`mvn test -P arq-jws-server-resteasy` - test CXF implementation  

For local testing, change <jws.home> parameter to point to existing tomcat instance and use:  
`mvn test -P arq-tomcat-resteasy` - test Resteasy implementation  
`mvn test -P arq-tomcat-cxf` - test CXF implementation  

##How it works:

Uses Arquillian with Tomcat managed instance.
When the maven test goal is run the appropriate server is unpacked into target directory and started.
Arquillian creates a separate WAR file for each test group containing all needed dependencies, deploys it to the server and executes the tests.

##High level structure:

*src/test/java/com/readhat*: contains all the tests, there is a package for each part of JAXRS specification.
There is a resources package for each one which contains utils and WAR file configurations.

*src/test/java/com/readhat/utils*: contains util classes used by the whole test suite

*src/test/java/resources/com/redhat*: various HTML and XML files needed by the test suite

*src/test/java/resources/arquillian.xml*: tomcat config file

*apache-tomcat-resteasy.zip*: preconfigured tomcat with resteasy

*apache-tomcat-cxf.zip*: preconfigured tomcat with CXF (not available yet)







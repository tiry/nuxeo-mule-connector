
This is a Nuxeo Connector for Mule that is based on Automation API.

Using this connector, you can build Mule Flows that will use services exposed by Nuxeo Platform.

Compared to a CMIS connector, it allows full access to Nuxeo API :

This includes :

 - CRUD on document repository
 - relations, tags, queries
 - workflows and tasks management
 - converters

but also
 
 - all the features that are not available via CMIS (complex properties, multi-blobs ...)
 - additional Custom Automation Operation (contributed via Nuxexo IDE)
 - additional Custom Automation Chains (contributed via Nuxeo Studio)

This connector exposes :

 - a predefined set of Operations
 - a runOperation that allows to call any Operation or Chain defined on the server
 - some converters to manage Nuxeo Automation objects 

## To build the plugin

Quick build 

> mvn -Ddevkit.studio.package.skip=false -Ddevkit.javadoc.check.skip -DskipTests=true  -Dmaven.test.skip=true clean package

Build with tests

> mvn -Ddevkit.studio.package.skip=false  clean package


Then use the update site generated in target/update-site to load the plugin from MuleStudio

NB : You have to use Maven 3 and Java 6 (you can 'source' env.sh for that)

## Update site

Our QA Chain does publish an update site based on the latest build :

http://qa.nuxeo.org/jenkins/job/nuxeo-mule-connector-master/lastStableBuild/artifact/target/update-site/


## Next steps

 - integrate OAuth support
 - extend list of build-in functions (code gen ?)
 - provide a Mule EndPoint to listen to Nuxeo events (mainly packaging)


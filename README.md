
This is a sample Nuxeo Connector for Mule.

It is based on Nuxeo Automation API.


## To build the plugin

> mvn -Ddevkit.studio.package.skip=false -Ddevkit.javadoc.check.skip  -DskipTests=true  -Dmaven.test.skip=true clean package

Then use the update site generated in target/update-site to load the plugin from MuleStudio

NB : You have to use Maven 3 and Java 6 (you can 'source' env.sh for that)


## Update site

Our QA Chain does publish an update site based on the latest build :

http://qa.nuxeo.org/jenkins/job/nuxeo-mule-connector-master/lastStableBuild/artifact/target/update-site/


 

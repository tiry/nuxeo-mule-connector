# nuxeo-mule-connector

This is a Nuxeo Connector for Mule that is based on Automation API.

Using this connector, you can build Mule Flows that will use services exposed by Nuxeo Platform.

This connector exposes :

 - a predefined set of Operations
 - a runOperation that allows to call any Operation or Chain defined on the Nuxeo server
 - some converters from Nuxeo objects (Document, Documents, Blob) to raw types (Maps, List of Maps, File ...)

## Connector design

### Nuxeo Automation Client

The connector is build on top of nuxeo Java Automation Client.

The automation client handles authentication and network marshaling.

### Static connector vs dynamic Nuxeo API

Nuxeo API is highly dynamic since :

 - addons can provide additionnal Operations
 - new Operations or chains can be added via Nuxeo Studio, Java or scripting.

The Mule Connector architecture is static and as is can not expose all Nuxeo API.

This connector exposes :

 - a predefined set of Operations
     - createDocument
     - getDocument
     - update
     - ...
 - a runOperation that allows to call any Operation or Chain defined on the Nuxeo server
     - allows to leverage custom Operations or Chains contributed by addons
 - some converters from Nuxeo objects (Document, Documents, Blob) to raw types (Maps, List of Maps, File ...)
 - an event listener (@Source) that allows to make Mule listen to Nuxeo events
     - it uses long polling http to fetch events from Audit log

### DataSense

The connector include a first level (very naive) implementation of DataSense for Nuxeo.

As a start Nuxeo Document Types are mapped to `MetaDataKey`.

Since most Nuxeo operation don't have a docType parameter there is no type/method mapping. 

Nuxeo Document are mapped via DataSense as a big Map :

 - Document attribute (name, parent, id, lifecycle ...) are mapped as `ecm:` properties
     - `doc.get('ecm:name')`
     - `doc.get('ecm:id')`
 - All first level properties are mapped as simple keys
     - `doc.get('dc:title)`
     - `doc.get('dc:created)`
 - Sub-properties are children of the first level properties
     - `doc.get('file:content').get('name')`

## Nuxeo Connector vs CMIS Connector

Compared to a CMIS connector, Automation based Nuxeo connector allows full access to Nuxeo API :

 - CRUD on document repository
 - relations, tags, queries
 - workflows and tasks management
 - converters

but also

 - all the features that are not available via CMIS (complex properties, multi-blobs ...)
 - additional Custom Automation Operation (contributed via Nuxexo IDE)
 - additional Custom Automation Chains (contributed via Nuxeo Studio)

## Build and install

### Target Mule version

The connector uses the lastest Mule API, 3.5, since we use @Source.

### To build the plugin

Quick build

> mvn -Ddevkit.studio.package.skip=false -Ddevkit.javadoc.check.skip -DskipTests=true -Dmaven.test.skip=true clean package 

Build with tests

> mvn -Ddevkit.studio.package.skip=false clean package

Then use the update site generated in target/update-site to load the plugin from MuleStudio

NB: You have to use Maven 3 and Java 6 (you can 'source' env.sh for that)
but the tests require Java 7 so you must also provide the path to the Java 7 if different than the default value (/usr/lib/jvm/jdk1.7.0_10).

### Update site

Our QA Chain does publish an update site based on the latest build :

http://qa.nuxeo.org/jenkins/job/nuxeo-mule-connector-master/lastStableBuild/artifact/target/update-site/

## Next steps

 - integrate OAuth support
 - extend list of build-in functions (code gen ?)



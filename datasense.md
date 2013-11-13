
# Nuxeo Mule Connector and DataSense

Here are some notes about Nuxeo and DataSense.

## Nuxeo Objects

### Output / Return objects

A lot of Nuxeo API will return a `Document` object.

A `Document` object is a java object with :

 - a set of fixed read-only fields :

      - getId()
      - getName()
      - getPath()
      - getState()
      - ...

 - a dynamic part returned as a `Map<String,Serializable>` via getProperties()

The structure of the dynamic part actually depends on the Document Type : each doc type is associated to a set a schemas and each schema contains a set of fields.

Nuxeo schemas are technically XSD schemas that supports scalars, multivalued fields and complex fields.

Here are some examples :

      document.getProperties().get("dc:description")

will return a `String`

      document.getProperties().get("dc:subjects")

will return a `List<String>`

      document.getProperties().get("file:content")

will return a `Blob` object (wrapper around a binary stream)

### Input objects

The `Document` object is never used as input of an API call :

 - either we simply send a reference : UUID or Path as String
 - or we only send a part of the Document : typically a part of the properties Map, only the part that needs to be changed

Typically, the `Document.Update` API will take as parameter a `PropertyMap` that can be constructed from a `Map<String,Serializable>` and contains only properties that must be changed.

## DataSense

### Types exposed by the Connector

For now, for each Document Type defined on the server, Nuxeo Connector exposes 2 types of objects :

 - Document object : the full Document object 
 - DataModel object : the `Map<String,Serializable>` properties object

This means that for a Server that has 2 document types Note and File, Mule connector will expose :

 - DOC_File
 - DM_File
 - DOC_Note
 - DM_Note

### Method bindings

A lot of Nuxeo API method will return `Document` objects, but is most of the case there is no method parameter that can be used to "guess" what will be the return Doc Type.

For some methods the return type will actually be a list of Documents that can all have different types : the obvious example is a search.

This means that for now, we don't use the Method binding : we just let the user select the Types directly inside the DataMapper.

Here is a sample configuration for a DataMapper : 

    Type : Connector
    Connector : Nuxeo
    By Type
    Type : DOC_FILE

### Field names and ":"

Nuxeo fields names use prefix : `dc:description`, `file:content`, `note:note` ...

When exposing these fields via DataSense, it looks like the generated *'Mapper script'* looks something like : 

    output.dc_title = input.title;
    output.dc_description = input.desc;

NB : here the *"Nuxeo object"* is the output. 

Does this mean that `":"` are prohibited in field names ?

### Wrapping the Document object

The Document object is not a `Map` but a Pojo containing a Map.

It looks like our Object should be accessible via the Map API : **is that correct ?**

If the answer is yes, I see 2 possibles approaches :

 - I can dynamically wrap all returned Document as an Object that will expose a Map like interface
 - May be I can also provide a "resolver" for the Mapper context ? (that's actually what we do in Nuxeo for EL, MVEL, FreeMarker ...)

I would prefer solution 2, but I can do solution 1 if needed.

### Building Nuxeo Object

When using a Mule Flow to build a Nuxeo Object via a DataMapper :

 - what will be the exact output java type for the DataMapper : a `Map<String,?>` ?
 - could I provide a contructor to build a '*real object'* or should I simply provide converters ?



 



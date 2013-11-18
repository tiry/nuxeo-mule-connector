package org.nuxeo.mule.metadata;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.nuxeo.ecm.automation.client.jaxrs.impl.HttpAutomationClient;

public class TypeDefinitionFecther {

    private static final String SCHEMAS_KEY = "schemas";

    private static final String DOCTYPES_KEY = "doctypes";

    protected HttpClient httpClient;

    protected JsonNode doctypes;
    protected JsonNode schemas;

    public TypeDefinitionFecther(HttpAutomationClient client) {
        if (client!=null) {
            httpClient = client.http();
        }
    }


    public JsonNode fetch() {

        InputStream jsonStream = null;

        // try to  fetch remote
        // XXX


        // fallback to local static definition
        try {
            jsonStream = this.getClass().getResource("/default-def/types.json").openStream();

            ObjectMapper mapper = new ObjectMapper();
            JsonFactory factory = new JsonFactory();
            JsonParser parser = factory.createJsonParser(jsonStream);

            return mapper.readTree(parser);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;

    }

    protected void doFetch()  {
        JsonNode root = fetch();
        if (root!=null) {
            doctypes = root.get(DOCTYPES_KEY);
            schemas = root.get(SCHEMAS_KEY);
        }
    }

    public JsonNode getDoctypes() {
        if (doctypes==null) {
            doFetch();
        }
        return doctypes;
    }

    public JsonNode getSchemas() {
        if (schemas==null) {
            doFetch();
        }
        return schemas;
    }

    public List<String> getDocTypesNames() {
        List<String> docNames = new ArrayList<String>();
        Iterator<String> names = getDoctypes().getFieldNames();
        while (names.hasNext()) {
            docNames.add(names.next());
        }
        return docNames;
    }

    public List<String> getSchemasForDocType(String docType) {
        List<String> schemas = new ArrayList<String>();
        JsonNode node = getDoctypes().get(docType);
        JsonNode schemasNode = node.get(SCHEMAS_KEY);
        for (int i = 0 ; i < schemasNode.size(); i++) {
            schemas.add(schemasNode.get(i).getTextValue());
        }
        return schemas;
    }

    public JsonNode getSchema(String schemaName) {
        return getSchemas().get(schemaName);
    }
}

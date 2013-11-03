package org.nuxeo.mule.metadata;

import java.util.HashMap;
import java.util.Map;

import org.mule.common.metadata.builder.DynamicObjectBuilder;
import org.mule.common.metadata.datatype.DataType;
import org.nuxeo.ecm.automation.client.Session;

/**
 * For testing purpose, this implementation is hard-coded and not wired to a REST API
 *
 * @author <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 *
 */
public class MetaDataIntrospector {

    private Session session;

    public MetaDataIntrospector(Session session) {
        this.session = session;
    }

    public String[] getDocTypes() {
        return new String[] {"File","Note","Folder","Workspace"};
    }

    public String[] getSchemasForDocType(String docType) {
        if ("File".equals(docType)) {
            return new String[] {"dublincore","common", "file"};
        }
        if ("Note".equals(docType)) {
            return new String[] {"dublincore","common", "note"};
        }
        if ("Folder".equals(docType)) {
            return new String[] {"dublincore","common"};
        }
        if ("Workspace".equals(docType)) {
            return new String[] {"dublincore","common", "file"};
        }

        // XXX

        return new String[] {"dublincore","common"};
    }

    public DataType getDataType(String nxFieldType) {

        if ("date".equalsIgnoreCase(nxFieldType)) {
            return DataType.DATE_TIME;
        }
        if ("long".equalsIgnoreCase(nxFieldType)) {
            return DataType.LONG;
        }
        if ("integer".equalsIgnoreCase(nxFieldType)) {
            return DataType.INTEGER;
        }

        if ("blob".equalsIgnoreCase(nxFieldType)) {
            return DataType.STREAM;
        }

        // XXX

        return DataType.STRING;
    }

    protected Map<String, String> getSchemaFields(String schemaName) {

        Map<String, String> fields = new HashMap<String, String>();

        if (schemaName.equalsIgnoreCase("dublincore")) {
            fields.put("dc:title", "string");
            fields.put("dc:description", "string");
            fields.put("dc:source", "string");
            fields.put("dc:created", "date");
            fields.put("dc:modified", "date");
            fields.put("dc:title", "string");
        }
        else if (schemaName.equalsIgnoreCase("common")) {
            fields.put("common:icon", "string");
            fields.put("common:size", "integer");
        }
        else if (schemaName.equalsIgnoreCase("file")) {
            fields.put("file:content", "blob");
            fields.put("file:filename", "string");
        }
        else if (schemaName.equalsIgnoreCase("note")) {
            fields.put("note", "string");
        }
        return fields;
    }

    public void mapSchema(DynamicObjectBuilder dynamicObject, String schemaName) {
        Map<String, String> fields = getSchemaFields(schemaName);
        for (String fieldName : fields.keySet()) {
            dynamicObject.addSimpleField(fieldName, getDataType(fields.get(fieldName)));
        }
    }
}

package org.nuxeo.mule.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.mule.common.metadata.DefaultMetaData;
import org.mule.common.metadata.DefaultMetaDataKey;
import org.mule.common.metadata.MetaData;
import org.mule.common.metadata.MetaDataKey;
import org.mule.common.metadata.MetaDataModel;
import org.mule.common.metadata.builder.DefaultMetaDataBuilder;
import org.mule.common.metadata.builder.DynamicObjectBuilder;
import org.mule.common.metadata.datatype.DataType;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.jaxrs.impl.HttpAutomationClient;

/**
 * For testing purpose, this implementation is hard-coded and not wired to a REST API
 *
 * @author <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 *
 */
public class MetaDataIntrospector {

    protected static final String DOC_PREFIX = "DOC_";
    protected static final String DATAMODEL_PREFIX = "DM_";

    protected TypeDefinitionFecther fetcher;

    public MetaDataIntrospector(Session session) {
        if (session!=null) {
            fetcher = new TypeDefinitionFecther((HttpAutomationClient) session.getClient());
        } else {
            // XXX tests !
            fetcher = new TypeDefinitionFecther(null);
        }
    }

    public List<String> getDocTypes() {
        return fetcher.getDocTypesNames();
    }

    public List<MetaDataKey> getMuleTypes() {
        List<MetaDataKey> types = new ArrayList<MetaDataKey>();
        for (String docType : getDocTypes()) {
            types.add(new DefaultMetaDataKey(docType, docType, false));
        }
        Collections.sort(types);
        return types;
    }


    public MetaData getMuleTypeMetaData(String key) {

        DynamicObjectBuilder dynamicObject = new DefaultMetaDataBuilder().createDynamicObject(key);
        String targetDocType = key;
        buildMuleDocTypeMetaDataModel(targetDocType, dynamicObject);
        MetaDataModel model = dynamicObject.build();

        return new DefaultMetaData(model);
    }

    protected void buildMuleDocTypeMetaDataModel(String docType, DynamicObjectBuilder dynamicObject) {
        dynamicObject.addSimpleField(DataSenseHelper.getDataSenseFieldName("ecm","path"), getDataType("string"));
        dynamicObject.addSimpleField(DataSenseHelper.getDataSenseFieldName("ecm","state"), getDataType("string"));
        dynamicObject.addSimpleField(DataSenseHelper.getDataSenseFieldName("ecm","repository"), getDataType("string"));
        dynamicObject.addSimpleField(DataSenseHelper.getDataSenseFieldName("ecm", "id"), getDataType("string"));
        dynamicObject.addSimpleField(DataSenseHelper.getDataSenseFieldName("ecm", "type"), getDataType("string"));
        buildMuleDataModelMetaDataModel(docType, dynamicObject);
    }

    protected void buildMuleDataModelMetaDataModel(String docType, DynamicObjectBuilder dynamicObject) {
        for (String schema : fetcher.getSchemasForDocType(docType)) {
            mapSchema(dynamicObject, schema);
        }
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
            return DataType.STRING;
        }

        // XXX

        return DataType.STRING;
    }

    protected void buildSchemaFields(String schemaName, DynamicObjectBuilder dynamicObject) {

        JsonNode schema = fetcher.getSchema(schemaName);

        JsonNode prefixNode = schema.get("@prefix");
        String prefix = schemaName;
        if (prefixNode!=null) {
            prefix = prefixNode.getTextValue();
        }
        if (prefix==null || prefix.isEmpty()) {
            prefix = schemaName;
        }

        Iterator<String> fieldNamesIt = schema.getFieldNames();
        while (fieldNamesIt.hasNext()) {
            String fieldName = fieldNamesIt.next();
            if (!fieldName.startsWith("@")) {
                JsonNode field = schema.get(fieldName);
                addField(prefix, fieldName, field, dynamicObject);
            }
        }
    }

    protected void addField(String prefix, String name , JsonNode fieldNode, DynamicObjectBuilder dynamicObject) {

        String type = fieldNode.getTextValue();
        JsonNode subFields = null;

        if (type==null && !fieldNode.isTextual()) {
            type = fieldNode.get("type").getTextValue();
            subFields = fieldNode.get("fields");
        }
        boolean multiValued = false;
        if (type.endsWith("[]")) {
            multiValued=true;
            type = type.substring(0, type.length()-2);
        }
        if ("blob".equals(type)) {
            DynamicObjectBuilder blobField = dynamicObject.addDynamicObjectField(DataSenseHelper.getDataSenseFieldName(prefix, name));
            blobField.addSimpleField("encoding", DataType.STRING);
            blobField.addSimpleField("mime-type", DataType.STRING);
            blobField.addSimpleField("name", DataType.STRING);
            blobField.addSimpleField("length", DataType.LONG);
            blobField.addSimpleField("digest", DataType.STRING);
            blobField.addSimpleField("data", DataType.STRING);
            return;
        }

        if (subFields!=null) {
            DynamicObjectBuilder cplxField = null;
            if (multiValued) {
                cplxField = dynamicObject.addListOfDynamicObjectField(DataSenseHelper.getDataSenseFieldName(prefix, name)).addDynamicObjectField("item");
            } else {
                cplxField = dynamicObject.addDynamicObjectField(DataSenseHelper.getDataSenseFieldName(prefix, name));
            }
            Iterator<String> fnames = subFields.getFieldNames();
            while (fnames.hasNext()) {
                String subFieldName = fnames.next();
                JsonNode subfield = subFields.get(subFieldName);
                addField(prefix, subFieldName, subfield, cplxField);
                //addField(prefix, name + "/" + subFieldName, subfield, cplxField);
            }
        } else {
            if (multiValued) {
                dynamicObject.addListOfDynamicObjectField(DataSenseHelper.getDataSenseFieldName(prefix, name)).addSimpleField("item", getDataType(type));
            } else {
                dynamicObject.addSimpleField(DataSenseHelper.getDataSenseFieldName(prefix, name), getDataType(type));
            }
        }
    }

    protected void mapSchema(DynamicObjectBuilder dynamicObject, String schemaName) {
        buildSchemaFields(schemaName, dynamicObject);
    }
}

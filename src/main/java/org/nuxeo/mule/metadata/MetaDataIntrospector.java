package org.nuxeo.mule.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mule.common.metadata.DefaultMetaData;
import org.mule.common.metadata.DefaultMetaDataKey;
import org.mule.common.metadata.MetaData;
import org.mule.common.metadata.MetaDataKey;
import org.mule.common.metadata.MetaDataModel;
import org.mule.common.metadata.builder.DefaultMetaDataBuilder;
import org.mule.common.metadata.builder.DynamicObjectBuilder;
import org.mule.common.metadata.builder.DynamicObjectFieldBuilder;
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

    protected static final String DOC_PREFIX = "DOC_";
    protected static final String DATAMODEL_PREFIX = "DM_";

    public MetaDataIntrospector(Session session) {
        this.session = session;
    }

    public String[] getDocTypes() {
        return new String[] {"File","Note","Folder","Workspace"};
    }

    public List<MetaDataKey> getMuleTypes() {
        List<MetaDataKey> types = new ArrayList<MetaDataKey>();
        for (String docType : getDocTypes()) {
            //types.add(new DefaultMetaDataKey(DOC_PREFIX + docType, DOC_PREFIX + docType, false));
            types.add(new DefaultMetaDataKey(DOC_PREFIX + docType, docType + " (doctype)", false));
            types.add(new DefaultMetaDataKey(DATAMODEL_PREFIX + docType, docType + " (datamodel)", false));
        }
        Collections.sort(types);
        return types;
    }


    public MetaData getMuleTypeMetaData(String key) {

        System.out.println("retrieve type " + key);

        DynamicObjectBuilder dynamicObject = new DefaultMetaDataBuilder().createDynamicObject(key);

        String targetDocType = key;
        if (targetDocType.startsWith(DOC_PREFIX)) {
            targetDocType = targetDocType.substring(DOC_PREFIX.length());
            buildMuleDocTypeMetaDataModel(targetDocType, dynamicObject);
        } else {
            targetDocType = targetDocType.substring(DATAMODEL_PREFIX.length());
            buildMuleDataModelMetaDataModel(targetDocType, dynamicObject);
        }
        MetaDataModel model = dynamicObject.build();

        System.out.println("type=>" + model.toString());

        return new DefaultMetaData(model);
    }

    protected void buildMuleDocTypeMetaDataModel(String docType, DynamicObjectBuilder dynamicObject) {
        //dynamicObject.addPojoField("path", String.class);
        dynamicObject.addPojoField("repository", String.class);
        dynamicObject.addPojoField("id", String.class);
        dynamicObject.addPojoField("type", String.class);
        //dynamicObject.addPojoField("state", String.class);

        dynamicObject.addSimpleField("path", getDataType("string"));
        dynamicObject.addSimpleField("state", getDataType("string"));

        DynamicObjectFieldBuilder mapField = dynamicObject.addDynamicObjectField("properties");
        buildMuleDataModelMetaDataModel(docType, mapField);
    }

    protected void buildMuleDataModelMetaDataModel(String docType, DynamicObjectBuilder dynamicObject) {
        for (String schema : getSchemasForDocType(docType)) {
            mapSchema(dynamicObject, schema);
        }
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

    protected void buildSchemaFields(String schemaName, DynamicObjectBuilder dynamicObject) {

        if (schemaName.equalsIgnoreCase("dublincore")) {
            dynamicObject.addSimpleField("dc:title", getDataType("string"));
            dynamicObject.addSimpleField("dc:description", getDataType("string"));
            dynamicObject.addSimpleField("dc:source", getDataType("string"));

            dynamicObject.addSimpleField("dc:created", getDataType("date"));
            dynamicObject.addSimpleField("dc:modified", getDataType("date"));

            dynamicObject.addListOfDynamicObjectField("dc:contributors").addSimpleField("item", getDataType("string"));

        }
        else if (schemaName.equalsIgnoreCase("common")) {
            dynamicObject.addSimpleField("common:icon", getDataType("string"));
            dynamicObject.addSimpleField("common:size", getDataType("integer"));
        }
        else if (schemaName.equalsIgnoreCase("file")) {
            dynamicObject.addSimpleField("file:content", getDataType("blob"));
            dynamicObject.addSimpleField("file:filename", getDataType("string"));
        }
        else if (schemaName.equalsIgnoreCase("note")) {
            dynamicObject.addSimpleField("note:note", getDataType("string"));
        }
    }

    protected void mapSchema(DynamicObjectBuilder dynamicObject, String schemaName) {
        buildSchemaFields(schemaName, dynamicObject);
    }
}

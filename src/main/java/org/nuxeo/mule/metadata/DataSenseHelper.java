package org.nuxeo.mule.metadata;

public class DataSenseHelper {

    public static final String SCHEMA_SEP = "__";

    public static String getDataSenseFieldName(String prefix, String fieldName) {
        return prefix + SCHEMA_SEP + fieldName;
    }

    public static String getNuxeoFieldName(String key) {
        int idx = key.indexOf(SCHEMA_SEP);
        if (idx > 0) {
            key = key.substring(0, idx) + ":"
                    + key.substring(idx + SCHEMA_SEP.length());
        }
        return key;
    }
}

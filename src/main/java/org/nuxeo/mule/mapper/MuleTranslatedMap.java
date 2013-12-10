package org.nuxeo.mule.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.nuxeo.mule.metadata.DataSenseHelper;

/**
 * Simple wrapper around a map to automatically translate field names for Mule
 * DataSense
 *
 * This also includes the logic for unwrapping Nuxeo complex properties
 * (PropertyMap and PropertyList).
 *
 * ex: 'dc:description' becomes 'dc__description'
 *
 * @author <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 *
 */
public class MuleTranslatedMap extends HashMap<String, Object> implements
        Map<String, Object> {

    private static final long serialVersionUID = 1L;

    @Override
    public boolean containsKey(Object key) {
        return super.containsKey(DataSenseHelper.getNuxeoFieldName((String) key));
    }

    @Override
    public Object get(Object key) {
        Object value = super.get(DataSenseHelper.getNuxeoFieldName((String) key));
        return value;
    }

    @Override
    public Object put(String key, Object val) {
        return super.put(DataSenseHelper.getNuxeoFieldName((String) key), val);
    }

    @Override
    public Object remove(Object key) {
        return super.remove(DataSenseHelper.getNuxeoFieldName((String) key));
    }

    protected Object unwrap(Object value) {
        if (value instanceof PropertyList) {
            ArrayList<Object> list = new ArrayList<Object>();
            for (Object item : ((PropertyList) value).list()) {
                list.add(unwrap(item));
            }
            return list;
        } else if (value instanceof PropertyMap) {
            MuleTranslatedMap v = new MuleTranslatedMap();
            v.putAll(((PropertyMap) value).map());
            return v;
        } else if (value instanceof Map<?, ?>) {
            MuleTranslatedMap v = new MuleTranslatedMap();
            v.putAll((Map<? extends String, ? extends Object>) value);
            return v;
        } else {
            return value;
        }
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> map) {
        for (String key : map.keySet()) {
            Object value = map.get(key);
            value = unwrap(value);
            put(key, value);
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        for (String k : keySet()) {
            sb.append(k + ":" + super.get(k) + ", ");
        }
        sb.append("}");
        return sb.toString();
    }

}

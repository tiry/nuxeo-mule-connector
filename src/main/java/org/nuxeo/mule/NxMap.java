package org.nuxeo.mule;

import java.util.HashMap;
import java.util.Map;

import org.nuxeo.mule.metadata.DataSenseHelper;

/**
 * Simple wrapper around a map to automatically translate
 * field names for Mule DataSense
 *
 * ex: 'dc:description' becomes 'dc__description'
 *
 * @author <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 *
 */
public class NxMap extends HashMap<String, Object> implements
        Map<String, Object> {

    private static final long serialVersionUID = 1L;

    @Override
    public boolean containsKey(Object key) {
        return super.containsKey(DataSenseHelper.getNuxeoFieldName((String) key));
    }

    @Override
    public Object get(Object key) {
        return super.get(DataSenseHelper.getNuxeoFieldName((String) key));
    }

    @Override
    public Object put(String key, Object val) {
        return super.put(DataSenseHelper.getNuxeoFieldName((String) key), val);
    }

    @Override
    public Object remove(Object key) {
        return super.remove(DataSenseHelper.getNuxeoFieldName((String) key));
    }

}

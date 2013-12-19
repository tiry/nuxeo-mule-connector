package org.nuxeo.mule.mapper;

import java.util.HashMap;
import java.util.Map;

import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.nuxeo.mule.metadata.DataSenseHelper;

public class MapUnmangler {

    public static PropertyMap unMangle(Map<String, Object> map) {
        Map<String, Object> pmap = new HashMap<String, Object>();
        for (String key : map.keySet()) {
            String xpath = DataSenseHelper.getNuxeoFieldName(key);
            if (xpath.startsWith("ecm:")) {
                continue;
            }
            pmap.put(xpath, map.get(key));
        }
        return new PropertyMap(pmap);
    }

}

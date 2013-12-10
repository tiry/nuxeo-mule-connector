package org.nuxeo.mule.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;

public class Doc2Map {

    public static Map<String, Object> documentToMap(Document doc) {
        Map<String, Object> map = new MuleTranslatedMap();
        map.put("ecm:type", doc.getType());
        map.put("ecm:facets", doc.getFacets().list());
        map.put("ecm:id", doc.getId());
        map.put("ecm:lock", doc.getLock());
        map.put("ecm:lockCreated", doc.getLockCreated());
        map.put("ecm:lockOwner", doc.getLockOwner());
        map.put("ecm:path", doc.getPath());
        map.put("ecm:repository", doc.getRepository());
        map.put("ecm:state", doc.getState());
        map.putAll(doc.getProperties().map());
        //  dump(map);
        return map;
    }

    public static List<Map<String, Object>> documentsToListOfMap(Documents docs) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (Document doc : docs) {
            result.add(documentToMap(doc));
        }
        return result;
    }
}

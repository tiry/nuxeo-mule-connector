package org.nuxeo.mule;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.nuxeo.ecm.automation.client.model.StringBlob;
import org.nuxeo.mule.mapper.MapUnmangler;
import org.nuxeo.mule.mapper.MuleTranslatedMap;

public class TestMappingHack {


    @Test
    public void testPrefixEscape() {

        String titleValue1 = "title1";
        Calendar dateValue1 = Calendar.getInstance();
        Blob blobValue1 = new StringBlob("Blob1");
        String[] arrayValue1 = new String[]{"toto", "titi"};

        // init map using Nuxeo interface name system
        MuleTranslatedMap nxMap = new MuleTranslatedMap();
        nxMap.put("dc:title", titleValue1);
        nxMap.put("dc:created", dateValue1);
        nxMap.put("file:content", blobValue1);
        nxMap.put("dc:contributors", arrayValue1);

        // verify access from prefixed names
        Assert.assertEquals(titleValue1, nxMap.get("dc:title"));
        Assert.assertEquals(dateValue1, nxMap.get("dc:created"));
        Assert.assertEquals(blobValue1, nxMap.get("file:content"));
        Assert.assertEquals(arrayValue1, nxMap.get("dc:contributors"));

        // verify access from Mule names
        Assert.assertEquals(titleValue1, nxMap.get("dc__title"));
        Assert.assertEquals(dateValue1, nxMap.get("dc__created"));
        Assert.assertEquals(blobValue1, nxMap.get("file__content"));
        Assert.assertEquals(arrayValue1, nxMap.get("dc__contributors"));

        //verify write back
        String titleValue2 = "title2";
        Calendar dateValue2 = Calendar.getInstance();
        Blob blobValue2 = new StringBlob("Blob2");
        String[] arrayValue2 = new String[]{"tata", "titi"};
        nxMap.put("dc__title", titleValue2);
        nxMap.put("dc__created", dateValue2);
        nxMap.put("file__content", blobValue2);
        nxMap.put("dc__contributors", arrayValue2);


        // verify access from prefixed names
        Assert.assertEquals(titleValue2, nxMap.get("dc:title"));
        Assert.assertEquals(dateValue2, nxMap.get("dc:created"));
        Assert.assertEquals(blobValue2, nxMap.get("file:content"));
        Assert.assertEquals(arrayValue2, nxMap.get("dc:contributors"));

        // verify access from Mule names
        Assert.assertEquals(titleValue2, nxMap.get("dc__title"));
        Assert.assertEquals(dateValue2, nxMap.get("dc__created"));
        Assert.assertEquals(blobValue2, nxMap.get("file__content"));
        Assert.assertEquals(arrayValue2, nxMap.get("dc__contributors"));

        // check unmangle

        nxMap.put("ecm:type", "type");
        nxMap.put("ecm:id", "uid");

        PropertyMap map2 = MapUnmangler.unMangle(nxMap);

        Assert.assertTrue(map2.getKeys().contains("dc:title"));
        Assert.assertTrue(map2.getKeys().contains("dc:created"));
        Assert.assertTrue(map2.getKeys().contains("file:content"));
        Assert.assertTrue(map2.getKeys().contains("dc:contributors"));

        Assert.assertFalse(map2.getKeys().contains("ecm:id"));





    }

}

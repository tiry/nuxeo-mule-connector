package org.nuxeo.mule;

import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.junit.Assert;
import org.junit.Test;
import org.mule.common.metadata.DefaultMetaData;
import org.mule.common.metadata.MetaData;
import org.mule.common.metadata.MetaDataModel;
import org.nuxeo.mule.metadata.MetaDataIntrospector;
import org.nuxeo.mule.metadata.TypeDefinitionFecther;

public class TestTypeFetcher {


    @Test
    public void checkTypeFetcher() throws Exception {

        TypeDefinitionFecther fetcher = new TypeDefinitionFecther(null);

        Assert.assertTrue(fetcher.getDocTypesNames().contains("File"));
        Assert.assertTrue(fetcher.getDocTypesNames().contains("DocumentRoute"));
        Assert.assertTrue(fetcher.getDocTypesNames().contains("Workspace"));

        List<String> schemas = fetcher.getSchemasForDocType("File");
        Assert.assertTrue(schemas.contains("dublincore"));
        Assert.assertTrue(schemas.contains("common"));
        Assert.assertTrue(schemas.contains("file"));

        JsonNode dcSchema = fetcher.getSchema("dublincore");
        Assert.assertNotNull(dcSchema);
    }


    @Test
    public void testMetaDataIntrospector() throws Exception {
        MetaDataIntrospector mi = new MetaDataIntrospector(null);
        List<String> docTypes = mi.getDocTypes();
        Assert.assertTrue(docTypes.contains("File"));
        Assert.assertTrue(docTypes.contains("DocumentRoute"));
        Assert.assertTrue(docTypes.contains("Workspace"));
        DefaultMetaData md = (DefaultMetaData) mi.getMuleTypeMetaData("File");
        MetaDataModel model = md.getPayload();
    }
}

package org.nuxeo.mule;

import java.io.File;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mvel2.ast.AssertNode;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.jaxrs.impl.HttpAutomationClient;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.IdRef;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.nuxeo.ecm.automation.client.model.StringBlob;
import org.nuxeo.ecm.automation.test.EmbeddedAutomationServerFeature;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.mule.blob.BlobConverters;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.Jetty;

import com.google.inject.Inject;


@RunWith(FeaturesRunner.class)
@Deploy({ "org.nuxeo.ecm.platform.types.api",
        "org.nuxeo.ecm.platform.types.core" })
@Features({ EmbeddedAutomationServerFeature.class })
@Jetty(port = 18080)
@RepositoryConfig(cleanup = Granularity.CLASS)
public class TestBlobsDownloadAndConversions {

    @Inject
    HttpAutomationClient client;

    @Test
    public void testBlobDownloadAndConvert() throws Exception {

        Session session = client.getSession("Administrator", "Administrator");
        Assert.assertNotNull(session);

        session.setDefaultSchemas("dublincore, common, file, files");
        DocumentService docService = session.getAdapter(DocumentService.class);

        // create simple file
        Document doc = docService.createDocument(new PathRef("/"), "File",
                "myfile");

        // attach a blob
        Blob stringBlob = new StringBlob("empty");
        stringBlob.setMimeType("text/plain");
        stringBlob.setFileName("somefile.txt");
        docService.setBlob(new IdRef(doc.getId()), stringBlob);

        // retrieve doc
        doc = docService.getDocument(new IdRef(doc.getId()));

        PropertyMap map = doc.getProperties().getMap("file:content");
        Assert.assertNotNull(map);

        Blob blob = BlobConverters.mapToBlob(session, map);

        Assert.assertNotNull(blob);

        Assert.assertNotNull(blob.getFileName());

        Assert.assertEquals("somefile.txt", blob.getFileName());


        File file = BlobConverters.blobToFile(blob);

        Assert.assertNotNull(file);
        Assert.assertTrue(file.exists());
        Assert.assertEquals(5, file.length());
        Assert.assertEquals("somefile.txt", file.getName());




    }
}

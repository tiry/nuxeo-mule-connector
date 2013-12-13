package org.nuxeo.mule;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.jaxrs.impl.HttpAutomationClient;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.IdRef;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.nuxeo.ecm.automation.client.model.StringBlob;
import org.nuxeo.ecm.automation.test.EmbeddedAutomationServerFeature;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.mule.blob.DownloadClient;
import org.nuxeo.mule.mapper.Doc2Map;
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
public class TestSimpleAutomationMapping {

    @Inject
    HttpAutomationClient client;

    @Test
    public void testGetAndMap() throws Exception {

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

        Blob stringBlob1 = new StringBlob("empty2");
        stringBlob1.setMimeType("text/plain");
        stringBlob1.setFileName("somefile1.txt");
        docService.setBlob(new IdRef(doc.getId()), stringBlob1, "files:files");

        // retrieve doc
        doc = docService.getDocument(new IdRef(doc.getId()));

        // generate the Mule Map
        Map<String, Object> map = Doc2Map.documentToMap(doc);

        // verify map content : complex type / blob
        Map<String, Object> content = (Map<String, Object>) map.get("file__content");
        Assert.assertNotNull(content);

        // check sub type
        String name = (String) content.get("name");
        Assert.assertEquals("somefile.txt", name);

        String blobUrl = (String) content.get("data");

        Assert.assertNotNull(blobUrl);

        // second content
        Map<String, Object> content2 = (Map<String, Object>) ((Map<String, Object>) ((List) map.get("files__files")).get(0)).get("file");
        Assert.assertNotNull(content2);

        // check sub type
        String name2 = (String) content2.get("name");
        String blobUrl2 = (String) content2.get("data");

        Assert.assertEquals("somefile1.txt", name2);
        Assert.assertNotNull(blobUrl2);

        // test download

        DownloadClient dc = new DownloadClient(client);

        InputStream in = dc.download(doc.getId(), "file:content");
        Scanner scanner = new Scanner(in).useDelimiter("\\A");
        Assert.assertNotNull(in);
        String fileContent = scanner.next();

        Assert.assertEquals("empty", fileContent);

        in = dc.download(blobUrl2);
        scanner = new Scanner(in).useDelimiter("\\A");
        Assert.assertNotNull(in);
        fileContent = scanner.next();

        Assert.assertEquals("empty2", fileContent);

    }

}

package org.nuxeo.mule.blob;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.InputStream;
import java.net.URLDecoder;

import org.apache.http.impl.client.BasicCookieStore;
import org.nuxeo.ecm.automation.client.jaxrs.impl.HttpAutomationClient;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.apache4.ApacheHttpClient4;
import com.sun.jersey.client.apache4.ApacheHttpClient4Handler;

public class DownloadClient {

    protected String apiURL;

    protected ApacheHttpClient4 client;

    public DownloadClient(HttpAutomationClient httpAutomationClient) {

        ApacheHttpClient4Handler handler = new ApacheHttpClient4Handler(
                httpAutomationClient.http(), new BasicCookieStore(), false);
        client = new ApacheHttpClient4(handler);

        if (httpAutomationClient.getRequestInterceptor() != null) {
            client.addFilter(httpAutomationClient.getRequestInterceptor());
        }
        apiURL = httpAutomationClient.getBaseUrl();
    }

    public InputStream download(String uid, String xpath) {

        WebResource wr = client.resource(apiURL);
        wr = wr.path("files");
        wr = wr.path(uid);
        wr = wr.queryParam("path", xpath);

        WebResource.Builder builder = wr.accept(APPLICATION_JSON);
        ClientResponse response = builder.get(ClientResponse.class);

        return response.getEntityInputStream();
    }

    public InputStream download(String urlPart) {
        String parts[] = urlPart.split("\\?");
        String uid = parts[0].substring(6);
        String path = parts[1].split("=")[1];
        path = URLDecoder.decode(path);
        return download(uid, path);
    }

}
